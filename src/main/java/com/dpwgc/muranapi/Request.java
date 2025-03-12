package com.dpwgc.muranapi;

public class Request {
    private String method;
    private String path;
    private Params query;
    private byte[] body;
    private Params headers;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public String getPath(int i) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        String[] ss = path.split("/");
        if (ss.length > i) {
            return ss[i];
        }
        return null;
    }

    public void setPath(String uri) {
        this.path = uri;
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
