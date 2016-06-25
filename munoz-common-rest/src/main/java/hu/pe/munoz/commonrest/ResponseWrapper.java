package hu.pe.munoz.commonrest;

/**
 * 
 * @author eatonmunoz
 *
 * @param <T> Generic type of data to return.
 */
public class ResponseWrapper<T> {

    private String status;
    private String message;
    private T data;

    /**
     * Constructs ResponseWrapper with empty arguments.
     */
    public ResponseWrapper() {}
    
    /**
     * Constructs ResponseWrapper with status, data, and message.
     * @param status Response status: SUCCESS or FAIL.
     * @param data Response data when SUCCESS.
     * @param message Response message when FAIL.
     */
    public ResponseWrapper(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    /**
     * Constructs ResponseWrapper with status and data.
     * @param status Response status: SUCCESS or FAIL.
     * @param data Response data when SUCCESS.
     */
    public ResponseWrapper(String status, T data) {
    	this.status = status;
    	this.data = data;
    }

    /**
     * Constructs ResponseWrapper with status and message.
     * @param status Response status: SUCCESS or FAIL.
     * @param message Response message when FAIL.
     */
    public ResponseWrapper(String status, String message) {
    	this.status = status;
    	this.message = message;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
