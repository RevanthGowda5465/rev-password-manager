package com.passwordmanager.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SimpleCipherUtil {

	// Secret key used for AES encryption
	private static final String KEY = "1234567890123456";

	// Initialization vector required for CBC mode
	private static final String IV = "abcdefghijklmnop";

	// Encrypts plain text and returns Base64 encoded string
	public static String encrypt(String value) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // Configure AES cipher
			SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES"); // Build AES key
			IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8")); // Set IV
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec); // Prepare cipher for encryption
			byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8")); // Encrypt data
			return Base64.getEncoder().encodeToString(encrypted); // Convert bytes to text
		} catch (Exception e) {
			throw new RuntimeException("Encryption failed: " + e.getMessage(), e);
		}
	}

	// Decrypts Base64 encoded text back into original value
	public static String decrypt(String value) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // Configure AES cipher
			SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES"); // Build AES key
			IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8")); // Set IV
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec); // Prepare cipher for decryption
			byte[] decoded = Base64.getDecoder().decode(value); // Decode Base64 string
			return new String(cipher.doFinal(decoded), "UTF-8"); // Convert bytes to plain text
		} catch (Exception e) {
			throw new RuntimeException("Decryption failed: " + e.getMessage(), e);
		}
	}
}
