package login;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encriptar {
	
	private static String salt = "Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";

	public static String md5(String entrada) {

		String md5 = null;

		if (entrada == null)
			return null;

		entrada = entrada + salt;
		
		try {

			// Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");

			// Update input string in message digest
			digest.update(entrada.getBytes(), 0, entrada.length());

			// Converts message digest value in base 16 (hex)
			md5 = new BigInteger(1, digest.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return md5;
	}
}
