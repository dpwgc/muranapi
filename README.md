# MuRanAPI

## 基于字典数实现路由功能的极简HTTP接口服务（无任何注解、反射、序列化、AOP逻辑，类似于Go的HttpRouter）

***

### 使用示例

```java
import com.dpwgc.muranapi.*;
import java.io.IOException;
import java.util.Arrays;

/**
 * 示例程序
 */
public class Demo {

    /**
     * 在8001端口运行HTTP服务
     */
    public static void main(String[] args) throws IOException {
        DemoController demo = new DemoController();
        new Router()
                .root("/v2")
                .get("/hello", demo::hello)
                .post("/submit", demo::submit)
                .group("/demo", new Group()
                        .get("/hello", demo::hello)
                        .post("/submit", demo::submit)
                )
                .error(System.out::println)
                .port(8001)
                .run();
    }

    /**
     * 示例接口方法
     */
    public static class DemoController {

        public Reply hello(Request request) {
            System.out.println(request.getQuery().map());
            return new Reply("hi");
        }

        public Reply submit(Request request) {
            System.out.println(Arrays.toString(request.getBody()));
            return new Reply("ok", new Params().set("Response-Header", "demo"));
        }
    }
}
```

* 运行程序后，访问接口地址

> [GET] http://127.0.0.1:8001/v2/hello

> [GET] http://127.0.0.1:8001/v2/demo/hello

> [POST] http://127.0.0.1:8001/v2/submit

> [POST] http://127.0.0.1:8001/v2/demo/submit