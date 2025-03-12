package com.dpwgc.muranapi;
import java.util.ArrayList;
import java.util.List;

public class Group {

    private final List<Triple<Method, String, Handler>> items = new ArrayList<>();

    public List<Triple<Method, String, Handler>> getItems() {
        return items;
    }

    public Group get(String path, Handler handler) {
        return this.add(Method.GET, path, handler);
    }

    public Group post(String path, Handler handler) {
        return this.add(Method.POST, path, handler);
    }

    public Group put(String path, Handler handler) {
        return this.add(Method.PUT, path, handler);
    }

    public Group patch(String path, Handler handler) {
        return this.add(Method.PATCH, path, handler);
    }

    public Group delete(String path, Handler handler) {
        return this.add(Method.DELETE, path, handler);
    }

    public Group head(String path, Handler handler) {
        return this.add(Method.HEAD, path, handler);
    }

    public Group options(String path, Handler handler) {
        return this.add(Method.OPTIONS, path, handler);
    }

    public Group add(Method method, String path, Handler handler) {
        items.add(new Triple<>(method, path, handler));
        return this;
    }
}
