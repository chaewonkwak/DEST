package Manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

public final class KeyManage implements Serializable {
	private static final long serialVersionUID = 1L;
	private static long ONE_GB = 1024L * 1024 * 1024; 		// 1GB

	public KeyManage() {};
	
	public Key generateKey(String algorithm, int keyLen) {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
			keyGen.init(keyLen);
			return keyGen.generateKey();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public KeyPair generateKeyPair(String algorithm, int keyLen) {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
			keyPairGen.initialize(keyLen);
			return keyPairGen.generateKeyPair();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean writeKey(String fname, Key key) {
		try (FileOutputStream fos = new FileOutputStream(fname)) {
			try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				oos.writeObject(key);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Key readKey(String fname) throws StrangeFileException {
		KeyManage.isValidFile(fname);
		try (FileInputStream fis = new FileInputStream(fname)) {
			try (ObjectInputStream ois = new ObjectInputStream(fis)) {
				Object obj = ois.readObject();
				if (obj instanceof Key) {
					Key key = (Key) obj;
					return key;
				} else {
					throw new StrangeObjectException();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (StrangeObjectException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	// 이상한 파일인지 검사하는 메소드 (예외 처리)
	public static void isValidFile(String fname) throws StrangeFileException {
			// DDos 공격을 막기 위한 파일 길이 제한
			File file = new File(fname);
			long fileSize = file.length();
			long maxSize = ONE_GB; 			// 1GB
			
			if (fileSize > maxSize) {
				System.out.println("========File Size Error (Maximum is 1GB)========");
				throw new StrangeFileException();
			}
			
			// 파일 확장자에 대한 whitelist 작성
			String[] allowedEXEs = { "jpg", "png", "key", "bin", "txt", "jpeg", "pdf", "csv", "xslx", "hwp", "pptx" };
	        String extension = fname.substring(fname.lastIndexOf(".") + 1);
	        for (String allowedEXE : allowedEXEs) {
	            if (extension.equalsIgnoreCase(allowedEXE)) {
	                return;
	            }
	        }
	        throw new StrangeFileException();
	}	 

}
