package chanlytech.paydemo.base;

/**
 * Created by Lyy on 2015/7/6.
 */
public class BaseEntity {
    private  int status;
    private int errorCode;
    private  String data;

    public String getErrorMsg() {
        return errorMsg;
    }

    public BaseEntity setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    private String errorMsg;//错误提示信息
    public int getStatus() {
        return status;
    }

    public BaseEntity setStatus(int status) {
        this.status = status;
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public BaseEntity setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getData() {
        return data;
    }

    public BaseEntity setData(String data) {
        this.data = data;
        return this;
    }
}
