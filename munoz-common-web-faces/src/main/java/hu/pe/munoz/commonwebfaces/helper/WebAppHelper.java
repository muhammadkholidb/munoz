package hu.pe.munoz.commonwebfaces.helper;

import java.io.IOException;
import java.util.Properties;

import javax.faces.context.ExternalContext;

public class WebAppHelper {

	// http://stackoverflow.com/questions/10873885/loading-properties-file-from-within-jar-file-with-jsf
	
	/**
	 * Assuming the file is in webContent or webapp folder.
	 * @param externalContext
	 * @param filename
	 * @return
	 */
	public static Properties getProperties(ExternalContext externalContext, String filename) {
		try {
			Properties prop = new Properties();
			prop.load(externalContext.getResourceAsStream(filename));
			return prop;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Assuming the file is in Java classpath.
	 * @param classLoader
	 * @param filename
	 * @return
	 */
	public static Properties getProperties(ClassLoader classLoader, String filename) {
		try {
			Properties prop = new Properties();
			prop.load(classLoader.getResourceAsStream(filename));
			return prop;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Properties getApplicationProperties(ClassLoader classLoader) {
		return getProperties(classLoader, "application.properties");
	}
	
}
