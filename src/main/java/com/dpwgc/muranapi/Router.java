package com.dpwgc.muranapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class Router {

    private final Map<String, Node> map = new ConcurrentHashMap<>();
    private int port = 8080;
    private int thread = 8;
    private String root = "/";
    private Error error;

    public Router get(String path, Handler handler) {
        return this.add(Method.GET, path, handler);
    }

    public Router post(String path, Handler handler) {
        return this.add(Method.POST, path, handler);
    }

    public Router put(String path, Handler handler) {
        return this.add(Method.PUT, path, handler);
    }

    public Router patch(String path, Handler handler) {
        return this.add(Method.PATCH, path, handler);
    }

    public Router delete(String path, Handler handler) {
        return this.add(Method.DELETE, path, handler);
    }

    public Router head(String path, Handler handler) {
        return this.add(Method.HEAD, path, handler);
    }

    public Router options(String path, Handler handler) {
        return this.add(Method.OPTIONS, path, handler);
    }

    public Router add(Method method, String path, Handler handler) {
        map.put(method + path, new Node(method, handler));
        return this;
    }

    public Router port(int port) {
        this.port = port;
        return this;
    }

    public Router thread(int thread) {
        this.thread = thread;
        return this;
    }

    public Router root(String root) {
        this.root = root;
        return this;
    }

    public Router error(Error error) {
        this.error = error;
        return this;
    }

    public Node match(String method, String path) {
        return map.get(method + path);
    }

    public void run() throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);
        httpServer.createContext(this.root, new IOHandler(this));
        httpServer.setExecutor(Executors.newFixedThreadPool(this.thread));
        httpServer.start();
    }

    private static class IOHandler implements HttpHandler {

        private final Router router;

        public IOHandler(Router router) {
            this.router = router;
        }

        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                Node node = router.match(httpExchange.getRequestMethod(), httpExchange.getRequestURI().getPath());

                if (node == null) {
                    byte[] nf = "not found".getBytes();
                    httpExchange.sendResponseHeaders(404, nf.length);
                    OutputStream out = httpExchange.getResponseBody();
                    out.write(nf);
                    out.flush();
                    out.close();
                    return;
                }

                Request request = new Request();
                request.setMethod(httpExchange.getRequestMethod());
                request.setUri(httpExchange.getRequestURI().getPath());

                request.setHeaders(new Params());
                request.setQuery(new Params());

                if (httpExchange.getRequestHeaders() != null && !httpExchange.getRequestHeaders().isEmpty()) {
                    for (String key : httpExchange.getRequestHeaders().keySet()) {
                        if (httpExchange.getRequestHeaders().get(key) != null && !httpExchange.getRequestHeaders().get(key).isEmpty()) {
                            request.getHeaders().set(key, httpExchange.getRequestHeaders().get(key).get(0));
                        }
                    }
                }

                if (httpExchange.getRequestURI().getQuery() != null && !httpExchange.getRequestURI().getQuery().isEmpty()) {
                    String[] pathArgs = httpExchange.getRequestURI().getQuery().split("&");
                    for (String param : pathArgs) {
                        String[] keyValue = param.split("=");
                        String key = URLDecoder.decode(keyValue[0], "UTF-8");
                        String value = URLDecoder.decode(keyValue[1], "UTF-8");
                        request.getQuery().set(key, value);
                    }
                }

                List<String> cls = httpExchange.getRequestHeaders().get("Content-Length");
                if (cls != null && !cls.isEmpty() && cls.get(0) != null && !cls.get(0).isEmpty()) {
                    int cl = Integer.parseInt(cls.get(0));
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead;
                    byte[] data = new byte[cl];
                    while ((nRead = httpExchange.getRequestBody().read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    request.setBody(buffer.toByteArray());
                }

                Reply reply = node.getHandler().execute(request);

                //设置响应头，必须在sendResponseHeaders方法之前设置！
                if (reply.getHeaders() != null) {
                    for (String key : reply.getHeaders().keySet()) {
                        httpExchange.getResponseHeaders().add(key, reply.getHeaders().get(key));
                    }
                }

                //设置响应码和响应体长度，必须在getResponseBody方法之前调用！
                httpExchange.sendResponseHeaders(reply.getCode(), reply.getBody().length);

                OutputStream out = httpExchange.getResponseBody();
                out.write(reply.getBody());
                out.flush();
                out.close();

            } catch (Exception ex) {
                if (router.error != null) {
                    router.error.execute(ex);
                }
            }
        }
    }
}
