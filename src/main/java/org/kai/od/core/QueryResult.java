package org.kai.od.core;

import org.kai.od.io.SerializableData;

public class QueryResult {

    private boolean success;

    private String msg;

    private SerializableData data;

    public QueryResult(boolean success, String msg) {
        this(success, msg, null);
    }

    public QueryResult(boolean success, String msg, SerializableData data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SerializableData getData() {
        return data;
    }

    public void setData(SerializableData data) {
        this.data = data;
    }
}
