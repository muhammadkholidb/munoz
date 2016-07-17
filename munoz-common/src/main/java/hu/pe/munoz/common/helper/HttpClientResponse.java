package hu.pe.munoz.common.helper;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class HttpClientResponse {

    private String response;
    private String status;
    private String message;
    private Object data;

    public HttpClientResponse(String response) {
        this.response = response;
        try {
            JSONObject json = (JSONObject) JSONValue.parseWithException(response);
            this.status = (String) json.get("status");
            this.message = (String) json.get("message");
            this.data = json.get("data");
        } catch (Exception e) {
            
        }
    }

    public String getResponse() {
        return response;
    }
    
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return getResponse();
    }

}
