package hu.pe.munoz.commonwebfaces.helper;

import org.omnifaces.util.Faces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHelper {

	private static Logger log = LoggerFactory.getLogger(MessageHelper.class);
	
	public static String getStringByEL(String var, String key) {
		String el = "#{" + var +"['" + key + "']}";
		return getStringByEL(el);
	}

	public static String getStringByEL(String el) {
		log.debug("Get EL: " + el);
		return Faces.evaluateExpressionGet(el);
	}
	
}
