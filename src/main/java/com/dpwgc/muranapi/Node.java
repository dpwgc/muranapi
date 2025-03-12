package com.dpwgc.muranapi;

public class Node {

    private final Handler handler;

    public Node(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return this.handler;
    }
}
