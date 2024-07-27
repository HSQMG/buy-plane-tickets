package com.tkpm.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashingUtil {

	//Hashing the password from a plain password
	public static String passwordEncryption(String plainPassword) {
		
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		if (null != digest) {
			byte[] hash = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
		}
		
		//Return dummy encrypted
		return "test123456";
	}
}
