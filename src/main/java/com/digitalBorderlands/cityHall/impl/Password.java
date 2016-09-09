package com.digitalBorderlands.cityHall.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {
	public static String hash(String password) {
		try {
			if ((password == null) || password.trim().isEmpty()) {
				return "";
			}
			
			byte[] bytes = password.getBytes();
			byte[] encoded = MessageDigest.getInstance("MD5").digest(bytes);
			StringBuffer sb = new StringBuffer();
			for (byte e : encoded) {
				sb.append(String.format("%02x", e));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
