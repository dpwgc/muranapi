package com.dpwgc.muranapi;

public class Request {
    private String method;
    private String uri;
    private Params query;
    private byte[] body;
    private Params headers;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Params getQuery() {
        return query;
    }

    public void setQuery(Params query) {
        this.query = query;
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

    public void setHeaders(Params header) {
        this.headers = header;
    }
}
