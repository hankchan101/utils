package cn.hankchan.utils.exception;

/**
 * @author hankChan
 *
 */
public class HttpClientApiException extends Exception {
    private static final long serialVersionUID = 1367036305732817703L;

    private String msg;
    private String code;
    private String detail;
    private String requestId;

    public HttpClientApiException() {
    }

    public HttpClientApiException(String msg) {
        this.msg = msg;
    }

    public HttpClientApiException(String msg, String detail) {
        this.msg = msg;
        this.detail = detail;
    }

    public HttpClientApiException(String msg, String code, String requestId) {
        this.msg = msg;
        this.code = code;
        this.requestId = requestId;
    }

    public HttpClientApiException(String msg, String code, String detail, String requestId) {
        this.msg = msg;
        this.code = code;
        this.detail = detail;
        this.requestId = requestId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
