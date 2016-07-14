package hu.pe.munoz.common.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient {

    private Logger log = LoggerFactory.getLogger(HttpClient.class);

    public static final String GET = "GET";
    public static final String POST = "POST";

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
        this.method = GET;
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

    public HttpClientResponse get() throws IllegalArgumentException {

        if ((method != null) && !GET.equals(method)) {
            throw new IllegalArgumentException("Invalid request method.");
        }
        
        if (method == null) {
            method = GET;
        }
        
        String strUrl = secure ? ("https://" + host + path) : ("http://" + host + path);
        String queryStrings = buildQueryStrings(parameters);

        log.debug("Sending GET request to URL : " + strUrl);
        log.debug("Parameters: " + parameters);

        try {

            URL url = new URL(strUrl + ((queryStrings == null) ? "" : ("?" + queryStrings)));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod(method);
            for (Object name : headers.keySet()) {
                con.setRequestProperty((String) name, (String) headers.get(name));
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
                return new HttpClientResponse(response.toString());
            }

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return null;
    }

    public HttpClientResponse post() throws IllegalArgumentException {

        if ((method != null) && !POST.equals(method)) {
            throw new IllegalArgumentException("Invalid request method.");
        }
        
        if (method == null) {
            method = POST;
        }
        
        String strUrl = secure ? ("https://" + host + path) : ("http://" + host + path);
        String queryStrings = buildQueryStrings(parameters);

        log.debug("Sending POST request to URL : " + strUrl);
        log.debug("Parameters : " + parameters);

        try {
            
            URL obj = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod(method);
            con.setDoOutput(true);
            for (Object name : headers.keySet()) {
                con.setRequestProperty((String) name, (String) headers.get(name));
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
                return new HttpClientResponse(response.toString());
            }

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return null;
    }

    public void addParameter(String name, String value) {
        if (parameters == null) {
            parameters = new JSONObject();
        }
        parameters.put(name, value);
    }
    
    public void setHeader(String name, String value) {
        if (headers == null) {
            headers = new JSONObject();
        }
        headers.put(name, value);
    }
    
    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public JSONObject getParameters() {
        return parameters;
    }

    public void setParameters(JSONObject parameters) {
        this.parameters = parameters;
    }

    public JSONObject getHeaders() {
        return headers;
    }

    public void setHeaders(JSONObject headers) {
        this.headers = headers;
    }

}
