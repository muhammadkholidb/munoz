package hu.pe.munoz.commonrest.helper;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

@Component
public class MessageString {

	@Autowired
	private MessageSource messageSource;
	
	private String code;
	private Object[] arguments;
	private Locale locale;
	
	public MessageString get(String code, Object[] arguments, Locale locale) {
		this.code = (code == null) ? "" : code;
		this.arguments = arguments;
		this.locale = (locale == null) ? Locale.getDefault() : locale;
		return this;
	}
	
	public MessageString get(String code, Object[] arguments) {
		return get(code, arguments, Locale.getDefault());
	}

	public MessageString get(String code, Locale locale) {
		return get(code, null, locale);
	}
		
	public MessageString get(String code) {
		return get(code, null, Locale.getDefault());
	}

	@Override
	public String toString() {
		if (code == null) {
			return "";
		}
		try {
			return messageSource.getMessage(code, arguments, locale);	
		} catch (NoSuchMessageException e) {
			return code;
		}
	}
}
