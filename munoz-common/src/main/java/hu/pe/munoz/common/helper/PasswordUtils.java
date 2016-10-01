package hu.pe.munoz.common.helper;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class PasswordUtils {

    public static final int DEFAULT_LEGTH = 64;
    
    public static String stirWithSalt(String actual, String salt, int length) {
        String sha1 = DigestUtils.sha1Hex(actual + salt); // 40 characters
        String rightPad = StringUtils.rightPad(sha1, length, DigestUtils.md5Hex(salt));
        return rightPad.substring(0, length); 
    }
    
    public static String stirWithSalt(String actual, String salt) {
        return stirWithSalt(actual, salt, DEFAULT_LEGTH);
    }
    
    public static String stir(String actual, int length) {
        String sha1 = DigestUtils.sha1Hex(actual); // 40 characters
        String rightPad = StringUtils.rightPad(sha1, length, DigestUtils.md5Hex(actual));
        return rightPad.substring(0, length);
    }
    
    public static String stir(String actual) {
        return stir(actual, DEFAULT_LEGTH);
    }
    
}
