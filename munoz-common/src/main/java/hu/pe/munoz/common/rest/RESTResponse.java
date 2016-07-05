package hu.pe.munoz.common.rest;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class RESTResponse {

    private String response;
    private String status;
    private String message;
    private Object data;

    public RESTResponse(String response) {
        this.response = response;
        JSONObject json = (JSONObject) JSONValue.parse(response);
        this.status = (String) json.get("status");
        this.message = (String) json.get("message");
        this.data = json.get("data");
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

    public String toString() {
        return response;
    }

}
