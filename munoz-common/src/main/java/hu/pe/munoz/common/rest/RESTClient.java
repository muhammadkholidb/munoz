package hu.pe.munoz.common.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RESTClient {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private boolean secure;
	private Map<String, String> headers;

	public RESTClient() {
		secure = false;
		headers = new HashMap<String, String>();
	}
	
	public void setHeader(String name, String value) {
		headers.put(name, value);
	}
	
	private String buildQueryStrings(JSONObject parameters) {
		if ((parameters != null) && !parameters.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			int i = 0;
			for (Object key : parameters.keySet()) {
				builder.append(key);
				builder.append("=");
				builder.append(parameters.get(key));
				i++;
				if (i < parameters.size()) {
					builder.append("&");
				}
			}
			return builder.toString();
		}
		return null;
	}

	public RESTResponse fetchGet(String host, String path, JSONObject parameters, boolean secure) {
		this.secure = secure;
		return fetchGet(host, path, parameters);
	}

	public RESTResponse fetchGet(String host, String path, JSONObject parameters) {

		String strUrl = secure ? ("https://" + host + path) : ("http://" + host + path);
		String queryStrings = buildQueryStrings(parameters);

		log.debug("Sending GET request to URL : " + strUrl);
		log.debug("Parameters: " + parameters);

		try {

			URL url = new URL(strUrl + ((queryStrings == null) ? "" : ("?" + queryStrings)));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");
			for (String name : headers.keySet()) {
				con.setRequestProperty(name, headers.get(name));
			}

			int responseCode = con.getResponseCode();
			log.debug("Response code : " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {

				StringBuilder response = new StringBuilder();
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				log.debug("Response: " + response);
				return new RESTResponse(response.toString());
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return null;
	}

	public RESTResponse fetchPost(String host, String path, JSONObject parameters, boolean secure) {
		this.secure = secure;
		return fetchPost(host, path, parameters);
	}
	
	public RESTResponse fetchPost(String host, String path, JSONObject parameters) {

		String strUrl = secure ? ("https://" + host + path) : ("http://" + host + path);
		String queryStrings = buildQueryStrings(parameters);

		log.debug("Sending POST request to URL : " + strUrl);
		log.debug("Parameters : " + parameters);

		try {
			URL obj = new URL(strUrl);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			for (String name : headers.keySet()) {
				con.setRequestProperty(name, headers.get(name));
			}

			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(queryStrings);
			wr.flush();
			wr.close();
			
			int responseCode = con.getResponseCode();
			log.debug("Response code: " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {				
				
				StringBuilder response = new StringBuilder();
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				log.debug("Response: " + response);
				return new RESTResponse(response.toString());
			}
			
		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return null;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

}
