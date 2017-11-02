package login;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encriptar {
	
	private static String salt = "Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";

	public static String md5(String entrada) {

		String md5 = null;

		if (entrada == null) // Si la cadena a encriptar no se encuentra inicializada
			return null;

		entrada = entrada + salt; // Agregamos el salt a la entrada
		
		try {

			// Creamos un objeto MessageDigest para MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");

			// Actualizamos la entrada mediante el MessageDigest
			digest.update(entrada.getBytes(), 0, entrada.length());

			// Convertimos el valor de MessageDigest en base 16(hexadecimal) 
			md5 = new BigInteger(1, digest.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return md5; // Retornamos la entrada encriptada
	}
}
