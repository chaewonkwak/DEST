package Manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

public final class SigManage {
	public SigManage() { };
	
	public Signature generate(String algorithm) {
		try {
			return Signature.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] makeSign(String input, Signature sig) throws StrangeFileException {
		KeyManage.isValidFile(input);
		try (FileInputStream fis = new FileInputStream(input)) {
			sig.update(fis.readAllBytes());
			return sig.sign();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] empty = {};
		return empty;
	}
	
	public boolean verifySign(byte[] signature, Signature sig) {
		try {
			return sig.verify(signature);
		} catch (SignatureException e) {
			e.printStackTrace();
		} 
		return false;
	}

}
