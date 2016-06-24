package hu.pe.munoz.common.helper;

import java.util.Random;

public class CommonUtils {

	private static final int DEFAULT_LENGTH = 8;

	public static String getRandomPassword(int length) {
		String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	public static String getDefaultRandomPassword() {
		return getRandomPassword(DEFAULT_LENGTH);
	}

	public static String toTitleCase(String text) {
		StringBuilder titleCase = new StringBuilder();
		boolean capitalize = true;
		for (char c : text.toLowerCase().toCharArray()) {
			if (Character.isSpaceChar(c)) {
				titleCase.append(c);
				capitalize = true;
				continue;
			}
			if (capitalize) {				
				c = Character.toUpperCase(c);
				capitalize = false;
			}
			titleCase.append(c);
		}

		return titleCase.toString();
	}

	public static void main(String[] args) {

		System.out.println(toTitleCase("title apa saja"));
		System.out.println(toTitleCase("TItle APA     saja"));
		
	}

}
