package hu.pe.munoz.common.rest;

import java.io.IOException;

import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.entity.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class RESTResponse {

    private String response;
    private String status;
    private String message;
    private Object data;

    public RESTResponse(HttpEntity httpEntity) {
        try {
            response = EntityUtils.toString(httpEntity);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
