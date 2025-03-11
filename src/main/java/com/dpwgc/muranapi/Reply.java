package com.dpwgc.muranapi;

public class Reply {

    private int code = 204;
    private byte[] body;
    private Params headers;

    public Reply() {

    }

    public Reply(byte[] body) {
        this.code = 200;
        this.body = body;
    }

    public Reply(int code, byte[] body) {
        this.code = code;
        this.body = body;
    }

    public Reply(int code, byte[] body, Params headers) {
        this.code = code;
        this.body = body;
        this.headers = headers;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Params getHeaders() {
        return headers;
    }

    public void setHeaders(Params headers) {
        this.headers = headers;
    }
}
