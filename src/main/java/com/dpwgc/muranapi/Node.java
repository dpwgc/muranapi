package com.dpwgc.muranapi;

public class Node {

    private final Method method;
    private final Handler handler;

    public Node(Method method, Handler handler) {
        this.method = method;
        this.handler = handler;
    }

    public Method getMethod() {
        return this.method;
    }

    public Handler getHandler() {
        return this.handler;
    }
}
