package hu.pe.munoz.commonrest.helper;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageHelper {

    @Autowired
    private MessageSource messageSource;

    private Locale defaultLocale;

    public void setDefaultLocale(Locale locale) {
        this.defaultLocale = locale;
    }

    public String getMessage(String code, Object[] args, Locale locale) {
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        return messageSource.getMessage(code, args, code, locale);
    }

    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, defaultLocale);
    }

    public String getMessage(String code) {
        return getMessage(code, null);
    }

}
