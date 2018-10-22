package cn.mt.gd.network;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {

    private int status;
    private String message;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
