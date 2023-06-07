package Manager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;

public final class CipherManage implements Serializable {
	private static final long serialVersionUID = 1L;

	public CipherManage() { };
	
	public Cipher generate(String algorithm) {
		try {
			return Cipher.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean writeCipher_file(String input, String output, Cipher c) {
		try (FileOutputStream fos = new FileOutputStream(output); 
				CipherOutputStream cos = new CipherOutputStream(fos,c)) {
			try (FileInputStream fis = new FileInputStream(input)) {
				cos.write(fis.readAllBytes());
				cos.flush();
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean writeCipher(byte[] input, String output, Cipher c) {
		try (FileOutputStream fos = new FileOutputStream(output); 
				CipherOutputStream cos = new CipherOutputStream(fos,c)) {
			cos.write(input);
			cos.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public StringBuffer readCipher(String fname, Cipher c) throws StrangeFileException {
		StringBuffer decrypted = new StringBuffer();
		
		KeyManage.isValidFile(fname);
		try (FileInputStream fis = new FileInputStream(fname);
				CipherInputStream cis = new CipherInputStream(fis,c); 
				BufferedReader br = new BufferedReader(new InputStreamReader(cis))) {
					
			String line = br.readLine();
			while (line != null) {
				decrypted.append(line);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return decrypted; 
	}	
	
	public byte[] readCipher_sig(String fname, Cipher c) throws StrangeFileException {
		KeyManage.isValidFile(fname);
		try (FileInputStream fis = new FileInputStream(fname);
				CipherInputStream cis = new CipherInputStream(fis,c);) {
				return cis.readAllBytes();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] empty = {};
		return empty; 
	}	

}
