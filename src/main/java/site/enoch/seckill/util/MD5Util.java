package site.enoch.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	
	private static final String salt = "1a2b3c4d";
	
	public static String inputPassToFormPass(String inputPass) {
		String str = ""+salt.charAt(0) + salt.charAt(1) + inputPass + salt.charAt(2) + salt.charAt(3);
		return md5(str);
	}
	
	public static String formPassToDBPass(String formPass, String salt) {
		String str = ""+salt.charAt(0) + salt.charAt(1) + formPass + salt.charAt(2) + salt.charAt(3);
		return md5(str);
	}

	public static String inputPassToDBPass(String inputPass, String saltDB) {
		String formPass = inputPassToFormPass(inputPass);
		String dbPass = formPassToDBPass(formPass, saltDB);
		return dbPass;
	}
	
	public static void main(String[] args) {
		//System.out.println(inputPassToFormPass("123456"));
		System.out.println(inputPassToDBPass("123456","1a2b3c"));
	}
}
