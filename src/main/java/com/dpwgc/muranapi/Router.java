package com.dpwgc.muranapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;

public class Router {

    private HttpServer server;
    private final TrieTree tree = new TrieTree();
    private int port = 8080;
    private int thread = 8;
    private String root = "";
    private Error error;
    private boolean debug = true;

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

    public Router any(String path, Handler handler) {
        for (Method method : Method.values()) {
            return this.add(method, path, handler);
        }
        return this;
    }

    public Router group(String path, Group group) {
        for (Triple<Method, String, Handler> item : group.getItems()) {
            this.add(item.getFirst(), path + item.getSecond(), item.getThird());
        }
        return this;
    }

    public Router add(Method method, String path, Handler handler) {
        if (path.contains("/:")) {
            path = path.split("/:")[0];
        }
        tree.insert(method.getName() + root + path, new Node(handler));
        return this;
    }

    public Node search(String method, String path) {
        return tree.search(method + path);
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

    public Router debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public Router server(HttpServer server) {
        this.server = server;
        return this;
    }

    private static final String MuRanAPI = "___  ___       ______                ___  ______  _____ \n" +
            "|  \\/  |       | ___ \\              / _ \\ | ___ \\|_   _|\n" +
            "| .  . | _   _ | |_/ / __ _  _ __  / /_\\ \\| |_/ /  | |  \n" +
            "| |\\/| || | | ||    / / _` || '_ \\ |  _  ||  __/   | |  \n" +
            "| |  | || |_| || |\\ \\| (_| || | | || | | || |     _| |_ \n" +
            "\\_|  |_/ \\__,_|\\_| \\_|\\__,_||_| |_|\\_| |_/\\_|     \\___/ \n";

    public void run() throws IOException {
        if (debug) {
            System.out.println("\033[34m" + MuRanAPI + "\033[0m");
        }
        if (server == null) {
            server = HttpServer.create(new InetSocketAddress(this.port), 0);
        }
        server.createContext("/", new IOHandler(this));
        server.setExecutor(Executors.newFixedThreadPool(this.thread));
        server.start();
    }

    private static class IOHandler implements HttpHandler {

        private final Router router;

        public IOHandler(Router router) {
            this.router = router;
        }

        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                Node node = router.search(httpExchange.getRequestMethod(), httpExchange.getRequestURI().getPath());

                if (node == null) {
                    response(httpExchange, 404, "not found".getBytes());
                    return;
                }

                Request request = new Request();
                request.setMethod(httpExchange.getRequestMethod());
                request.setPath(httpExchange.getRequestURI().getPath());

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

                Reply reply = null;
                try {
                    reply = node.getHandler().execute(request);
                } catch (Throwable e) {
                    error(e);
                    response(httpExchange, 500, "internal server error".getBytes());
                    return;
                }

                // 响应头
                if (reply.getHeaders() != null) {
                    for (String key : reply.getHeaders().keySet()) {
                        httpExchange.getResponseHeaders().add(key, reply.getHeaders().get(key));
                    }
                }
                // 返回
                response(httpExchange, reply.getCode(), reply.getBody());
            } catch (Throwable oe) {
                error(oe);
            }
        }

        private void error(Throwable e) {
            if (e == null) {
                return;
            }
            if (router.debug) {
                e.printStackTrace();
            }
            if (router.error != null) {
                router.error.execute(e);
            }
        }

        private void response(HttpExchange httpExchange, int code, byte[] body) throws IOException {
            if (router.debug) {
                System.out.printf("\033[32m[%s]\033[0m %s%s -> %s%n", httpExchange.getRequestMethod(), router.port, httpExchange.getRequestURI().getPath(), code);
            }
            httpExchange.sendResponseHeaders(code, body.length);
            OutputStream out = httpExchange.getResponseBody();
            out.write(body);
            out.flush();
            out.close();
        }
    }
}
