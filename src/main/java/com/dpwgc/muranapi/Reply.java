package com.dpwgc.muranapi;

public class Reply {

    private int code = 204;
    private byte[] body;
    private Params headers;

    public Reply() {

    }

    public Reply(String body) {
        if (body != null && !body.isEmpty()) {
            this.code = 200;
            this.body = body.getBytes();
        } else {
            this.body = "".getBytes();
        }
    }

    public Reply(byte[] body) {
        if (body != null && body.length > 0) {
            this.code = 200;
            this.body = body;
        } else {
            this.body = "".getBytes();
        }
    }

    public Reply(int code, byte[] body) {
        this.code = code;
        this.body = body;
    }

    public Reply(int code, String body) {
        this.code = code;
        if (body != null) {
            this.body = body.getBytes();
        } else {
            this.body = "".getBytes();
        }
    }

    public Reply(byte[] body, Params headers) {
        if (body != null && body.length > 0) {
            this.code = 200;
            this.body = body;
        } else {
            this.body = "".getBytes();
        }
        this.headers = headers;
    }

    public Reply(String body, Params headers) {
        if (body != null && !body.isEmpty()) {
            this.code = 200;
            this.body = body.getBytes();
        } else {
            this.body = "".getBytes();
        }
        this.headers = headers;
    }

    public Reply(int code, byte[] body, Params headers) {
        this.code = code;
        this.body = body;
        this.headers = headers;
    }

    public Reply(int code, String body, Params headers) {
        this.code = code;
        this.body = body.getBytes();
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
