package hu.pe.munoz.common.helper;

import java.util.Random;

public class CommonUtils {

    public static String getRandomAlphanumeric(int length) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static String getExceptionMessage(Exception exception) {
        String message = exception.getMessage();
        return ((message == null) || "".equals(message)) ? exception.toString() : message;
    }
    
}
