package hu.pe.munoz.common.rest;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.sync.CloseableHttpClient;
import org.apache.hc.client5.http.impl.sync.HttpClients;
import org.apache.hc.client5.http.methods.CloseableHttpResponse;
import org.apache.hc.client5.http.methods.HttpGet;
import org.apache.hc.client5.http.methods.HttpPost;
import org.apache.hc.client5.http.utils.URIBuilder;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;

public class RESTClient implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean secure = false;

    public void doGet(String host, String path, JSONObject parameters, boolean secure) {
        this.secure = secure;
        doGet(host, path, parameters);
    }

    public RESTResponse doGet(String host, String path, JSONObject parameters) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            URI uri = buildURI(secure ? "https" : "http", host, path, parameters);
            HttpGet httpGet = new HttpGet(uri);
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            return new RESTResponse(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void doPost(String host, String path, JSONObject parameters, boolean secure) {
        this.secure = secure;
        doPost(host, path, parameters);
    }
    
    public RESTResponse doPost(String host, String path, JSONObject parameters) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            URI uri = buildURI(secure ? "https" : "http", host, path, null);
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (Object key : parameters.keySet()) {
                list.add(new BasicNameValuePair(key.toString(), parameters.get(key).toString()));
            }
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(new UrlEncodedFormEntity(list)); 
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            return new RESTResponse(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private URI buildURI(String scheme, String host, String path, JSONObject parameters)
        throws URISyntaxException {

        URI uri = null;
        URIBuilder uriBuilder = new URIBuilder().setScheme(secure ? "https" : "http").setHost(host).setPath(path);
        if (parameters != null) {
            for (Object key : parameters.keySet()) {
                uriBuilder.setParameter(key.toString(), parameters.get(key).toString());
            }
        }
        uri = uriBuilder.build();
        return uri;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

}
