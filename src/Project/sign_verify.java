package Project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import Manager.CipherManage;
import Manager.KeyManage;
import Manager.SigManage;
import Manager.StrangeFileException;
import Manager.StrangeObjectException;

final class sign_verify {
	private static String signAlgorithm = "SHA256withRSA";	// 서명 생성에 사용할 알고리즘 : 해시 + 비대칭암호
	private static String cipherAlgorithm = "AES";			// 평문(메세지)과 서명 암호화에 사용할 알고리즘 : 대칭암호
	private static String keyAlgorithm = "RSA";				// 위에서 사용한 secretKey 암호화에 사용할 알고리즘 : 비대칭암호
	
	private static int keyLen = 1024; 		//  RSA key size
	private static int keySize = 256;  		//  AES key size
	
	private static KeyManage kmanager = new KeyManage();
	private static SigManage smanager = new SigManage();
	private static CipherManage cmanager = new CipherManage();

	static boolean generateKey(String fname) {		
		Key secretKey = kmanager.generateKey(cipherAlgorithm, keySize);
		
		if (secretKey != null) {
			kmanager.writeKey(fname, secretKey);
			return true;
		}
		return false;
	}
	
	static boolean generateKeyPair(String fname1, String fname2) {		
		KeyPair keypair = kmanager.generateKeyPair(keyAlgorithm, keyLen);
		
		if (keypair != null) {
			PublicKey publicKey = keypair.getPublic();
			PrivateKey privateKey = keypair.getPrivate();
			
			// 공개키, 개인키 파일에 저장하기
			kmanager.writeKey(fname1, publicKey);
			kmanager.writeKey(fname2, privateKey);
			return true;
		}
		return false;
	}
	
	static boolean sign(String msg, String pkey, String skey, String rPkey, String ename) {
		Signature sig = smanager.generate(signAlgorithm);
		
		PrivateKey privatekey = null;	// sender의 private key
		PublicKey publickey = null;		// receiver의 public key
		Key secretKey = null;			// 대칭키
		
		// key 읽어오기
		try {
			if (kmanager.readKey(pkey) instanceof PrivateKey && kmanager.readKey(rPkey) instanceof PublicKey && kmanager.readKey(skey) instanceof Key) {
				privatekey = (PrivateKey) kmanager.readKey(pkey);    
				publickey = (PublicKey) kmanager.readKey(rPkey);   	 
				secretKey = kmanager.readKey(skey);						 	
			} else {
				throw new StrangeObjectException();
			}
		} catch (StrangeFileException e) {
			// System.out.println(e.getMessage()); 이상한 파일이 들어왔을 때, 크기 문제가 아니라면 공격자가 왜 오류가 났는지 알 수 없도록 숨기자.
		} catch (StrangeObjectException e) {
			System.out.println(e.getMessage());
		}
		
		if (sig!= null && privatekey != null && publickey != null && secretKey != null) {
						
			try {
				sig.initSign(privatekey);
				byte[] signature = smanager.makeSign(msg, sig);
				
				if (signature == null) { 
					System.out.println("Fatal Error: signature is null"); 
				}			
				else {					
					Cipher c = cmanager.generate(cipherAlgorithm);
					if (c != null) {
						c.init(Cipher.ENCRYPT_MODE, secretKey);
							
						if (cmanager.writeCipher(signature, ename + "_sig.txt", c)) {
							// 생성한 서명은 사용 후 지워주기
							for (int i = 0; i < signature.length; i++) {
								signature[i] = 0;
							}
							System.out.println("Your signature is successfully generated and encrypted : " + ename + "_sig.txt");
						}
						if (cmanager.writeCipher_file(msg, ename + "_message.txt", c)) {
							System.out.println("The plaintext is successfully encrypted : " + ename + "_message.txt");
						}
						c = Cipher.getInstance(keyAlgorithm);	
						c.init(Cipher.ENCRYPT_MODE, publickey);
		
						try(FileOutputStream fos = new FileOutputStream(ename + "_key.txt")) {
								byte[] encrypted = c.doFinal(secretKey.getEncoded());
								fos.write(encrypted);
								// 사용한 secretKey는 사용 후 지워주기 (가비지 컬렉터가 빨리 회수하게끔 null로)
								secretKey = null; 
								System.out.println("The secretKey is successfully encrypted (using receiver's public key): " + ename + "_key.txt");
								return true;
						} catch (IOException e) {
							e.printStackTrace();
						} catch (IllegalBlockSizeException e) {
							e.printStackTrace();
						} catch (BadPaddingException e) {
							e.printStackTrace();
						} 
					}
				}
			
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (StrangeFileException e) {
				 e.printStackTrace(); 		
			}
		}
		return false;

	}
	
	static boolean vrfy(String rpkey, String Pkey, String ename) {
		try {
			// secretKey 해독에는 대칭암호 : RSA 알고리즘 사용
			Cipher c = Cipher.getInstance(keyAlgorithm);
			if (c != null) {
				c.init(Cipher.DECRYPT_MODE, kmanager.readKey(rpkey));
				
				// key 읽어오기 (여기서도 Dos 공격을 피하기 위해 파일 검사 필요)
				try (FileInputStream fis = new FileInputStream(ename + "_key.txt")) {
					byte[] encrypted = fis.readAllBytes();
					byte[] decrypted = c.doFinal(encrypted);
					
					Key secretKey = new SecretKeySpec(decrypted, "AES");
					
					// 복호화한 키는 사용 후 지워주기
					for (int i = 0; i < decrypted.length; i++) {
						decrypted[i] = 0;
					}
					
					// 서명과 평문 해독에는 대칭 암호 : AES 알고리즘 사용
					c= Cipher.getInstance(cipherAlgorithm);
					if (c != null) {
						c.init(Cipher.DECRYPT_MODE, secretKey);
						
						byte[] signature = cmanager.readCipher_sig(ename + "_sig.txt", c);
		
						Signature sig = Signature.getInstance(signAlgorithm);
						if (kmanager.readKey(Pkey) instanceof PublicKey) {
							sig.initVerify((PublicKey) kmanager.readKey(Pkey));
						} else {
							throw new StrangeObjectException();
						}
						byte[] message = cmanager.readCipher_sig(ename + "_message.txt", c);
						sig.update(message);
						
						// 복호화한 메세지는 사용 후 지워주기
						for (int i = 0; i < message.length; i++) {
							message[i] = 0;
						}
						return smanager.verifySign(signature, sig);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				} catch (StrangeObjectException e) {
					System.out.println(e.getMessage());	
				}
			}
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (StrangeFileException e) {
			e.printStackTrace();
		} 
		return false;
	}
}

