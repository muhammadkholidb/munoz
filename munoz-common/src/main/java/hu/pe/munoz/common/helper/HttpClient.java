package hu.pe.munoz.common.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

    public static final String GET = "GET";
    public static final String POST = "POST";

    private final String HTTP_SCHEME = "http";
    private final String HTTPS_SCHEME = "https";
    
    private String method;
    private String host;
    private String path;
    private JSONObject parameters;
    private JSONObject headers;
    private boolean secure;

    public HttpClient() {}

    public HttpClient(String host, String path) {
        this(host, path, null);
    }
    
    public HttpClient(String host, String path, JSONObject parameters) {
        this(host, path, parameters, null);
    }

    public HttpClient(String host, String path, JSONObject parameters, JSONObject headers) {
        this(host, path, parameters, headers, false);
    }

    public HttpClient(String host, String path, JSONObject parameters, JSONObject headers, boolean secure) {
        this.host = host;
        this.path = path;
        this.parameters = parameters;
        this.headers = headers;
        this.secure = secure;
    }

//    public HttpClient(String url) {
//        processUrl(url);
//    }
//
//    private void processUrl(String url) {
//        // https://docs.oracle.com/javase/tutorial/networking/urls/urlInfo.html
//
//    }

    public HttpClientResponse request() throws IOException {
        switch (method) {
            case POST:
                return post();
            case GET:
                return get();
            default:
                throw new UnsupportedOperationException("Unsupported request method: " + method );
        }
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

    public HttpClientResponse get() throws IOException {
    	
        method = GET;
        
        if ((host == null) || "".equals(host)) {
        	throw new NullPointerException("Empty host.");
        }
        
        String prefixSchemeHttp = HTTP_SCHEME + "://";
        String prefixSchemeHttps = HTTPS_SCHEME + "://";
        
        if (host.toLowerCase().startsWith(prefixSchemeHttp)) {
        	host = host.substring(prefixSchemeHttp.length() - 1);
        	secure = false;
        } else if (host.toLowerCase().startsWith(prefixSchemeHttps)) {
        	host = host.substring(prefixSchemeHttps.length() - 1);
        	secure = true;
        }
        
        String strUrl = secure ? (prefixSchemeHttps + host + path) : (prefixSchemeHttp + host + path);
        String queryStrings = buildQueryStrings(parameters);

        // Read http://slf4j.org/faq.html#logging_performance
        LOG.debug("Sending GET request to URL: {}", strUrl);
        LOG.debug("Parameters: {}", parameters);

        URL url = new URL(strUrl + ((queryStrings == null) ? "" : ("?" + queryStrings)));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod(method);
        if (headers != null) {
            LOG.debug("Headers: {}", headers);
            for (Object name : headers.keySet()) {
                con.setRequestProperty((String) name, (String) headers.get(name));
            }
        }

        int responseCode = con.getResponseCode();
        LOG.debug("Response code: {}", responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {

            StringBuilder response = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            LOG.debug("Response: {}", response);
            return new HttpClientResponse(response.toString());
        }

        return null;
    }

    public HttpClientResponse post() throws IOException {
    	
        method = POST;

        if ((host == null) || "".equals(host)) {
        	throw new NullPointerException("Empty host.");
        }
        
        String prefixSchemeHttp = HTTP_SCHEME + "://";
        String prefixSchemeHttps = HTTPS_SCHEME + "://";
        
        if (host.toLowerCase().startsWith(prefixSchemeHttp)) {
        	host = host.substring(prefixSchemeHttp.length() - 1);
        	secure = false;
        } else if (host.toLowerCase().startsWith(prefixSchemeHttps)) {
        	host = host.substring(prefixSchemeHttps.length() - 1);
        	secure = true;
        }
        
        String strUrl = secure ? (prefixSchemeHttps + host + path) : (prefixSchemeHttp + host + path);
        String queryStrings = buildQueryStrings(parameters);

        LOG.debug("Sending POST request to URL: {}", strUrl);
        LOG.debug("Parameters: {}", parameters);

        URL obj = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod(method);
        con.setDoOutput(true);
        if (headers != null) {
            LOG.debug("Headers: {}", headers);
            for (Object name : headers.keySet()) {
                con.setRequestProperty((String) name, (String) headers.get(name));
            }
        }

        if (queryStrings != null) {
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(queryStrings);
            wr.flush();
            wr.close();    
        }

        int responseCode = con.getResponseCode();
        LOG.debug("Response code: {}", responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {

            StringBuilder response = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            LOG.debug("Response: {}", response);
            return new HttpClientResponse(response.toString());
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public HttpClient addParameter(String name, Object value) {
        if (parameters == null) {
            parameters = new JSONObject();
        }
        parameters.put(name, value);
        return this;
    }
    
    public Object getParameter(String name) {
        if (parameters != null) {
            return parameters.get(name);
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public HttpClient setHeader(String name, Object value) {
        if (headers == null) {
            headers = new JSONObject();
        }
        headers.put(name, value);
        return this;
    }
    
    public Object getHeader(String name) {
        if (headers != null) {
            return headers.get(name);
        }
        return null;
    }
    
    public boolean isSecure() {
        return secure;
    }

    public HttpClient setSecure(boolean secure) {
        this.secure = secure;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public HttpClient setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getHost() {
        return host;
    }

    public HttpClient setHost(String host) {
        this.host = host;
        return this;
    }

    public String getPath() {
        return path;
    }

    public HttpClient setPath(String path) {
        this.path = path;
        return this;
    }

    public JSONObject getParameters() {
        return parameters;
    }

    public HttpClient setParameters(JSONObject parameters) {
        this.parameters = parameters;
        return this;
    }

    public JSONObject getHeaders() {
        return headers;
    }

    public HttpClient setHeaders(JSONObject headers) {
        this.headers = headers;
        return this;
    }

}
