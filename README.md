# MuRanAPI

## 一个基于字典数实现路由功能的极简HTTP接口服务（无任何注解、反射、序列化、AOP逻辑，类似于Go的HttpRouter）

***

### 使用示例

```java
import com.dpwgc.muranapi.*;
import com.sun.net.httpserver.HttpsServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * 示例程序
 */
public class Demo {

    /**
     * 在8001端口运行HTTP服务
     * 一共5个接口，分别是：
     * /v2/hello
     * /v2/submit
     * /v2/demo/hello
     * /v2/demo/submit
     * /v2/pathValue/123456
     */
    public static void main(String[] args) throws IOException {
        DemoController demo = new DemoController();
        new Router()
                // 所有接口的公共前缀
                .root("/v2")
                // 常规接口
                .get("/hello", demo::hello)
                .post("/submit", demo::submit)
                // 具有相同路径前缀的接口组
                .group("/demo", new Group()
                        .get("/hello", demo::hello)
                        .post("/submit", demo::submit)
                )
                // 带有路径参数的接口
                .get("/pathValue/:id", demo::pathValue)
                // 异常处理方法
                .error(System.out::println)
                // 端口号
                .port(8001)
                // 自定义HTTPS服务
                // .server(HttpsServer.create(new InetSocketAddress(443), 0))
                // 启动服务
                .run();
    }

    /**
     * 示例接口方法
     */
    public static class DemoController {

        /**
         * GET请求接口
         */
        public Reply hello(Request request) {
            System.out.println("query: " + request.getQuery().map());
            return new Reply("hi");
        }

        /**
         * POST请求接口
         */
        public Reply submit(Request request) {
            System.out.println("body: " + Arrays.toString(request.getBody()));
            // 携带自定义响应头
            return new Reply("ok", new Params().set("Response-Header", "demo"));
        }

        /**
         * 带路径参数的GET请求接口
         */
        public Reply pathValue(Request request) {
            System.out.println("id: " + request.getPath(3));
            return new Reply("hi");
        }
    }
}
```

* 运行程序后，访问接口地址

> [GET] http://127.0.0.1:8001/v2/hello

> [GET] http://127.0.0.1:8001/v2/demo/hello

> [POST] http://127.0.0.1:8001/v2/submit

> [POST] http://127.0.0.1:8001/v2/demo/submit
> [GET] http://127.0.0.1:8001/v2/pathValue/123456