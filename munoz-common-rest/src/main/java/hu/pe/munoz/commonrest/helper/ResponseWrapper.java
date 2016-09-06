package hu.pe.munoz.commonrest.helper;

/**
 *
 * @author eatonmunoz
 *
 */
public class ResponseWrapper {

    private String status;
    private String message;
    private Object data;

    /**
     * Constructs ResponseWrapper with empty arguments.
     */
    public ResponseWrapper() {
    }

    /**
     * Constructs ResponseWrapper with status, data, and message.
     *
     * @param status Response status: SUCCESS or FAIL.
     * @param data Response data when SUCCESS.
     * @param message Response message when FAIL.
     */
    public ResponseWrapper(String status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    /**
     * Constructs ResponseWrapper with status and data.
     *
     * @param status Response status: SUCCESS or FAIL.
     * @param data Response data when SUCCESS.
     */
    public ResponseWrapper(String status, Object data) {
        this(status, data, "");
    }

    /**
     * Constructs ResponseWrapper with status and message.
     *
     * @param status Response status: SUCCESS or FAIL.
     * @param message Response message when FAIL.
     */
    public ResponseWrapper(String status, String message) {
        this(status, "", message);
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
