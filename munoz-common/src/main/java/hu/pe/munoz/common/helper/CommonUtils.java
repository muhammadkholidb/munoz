package hu.pe.munoz.common.helper;

public class CommonUtils {

    public static String getExceptionMessage(Exception exception) {
        String message = exception.getMessage();
        return ((message == null) || "".equals(message)) ? exception.toString() : message;
    }
    
}
