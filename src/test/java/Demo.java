import com.dpwgc.muranapi.Params;
import com.dpwgc.muranapi.Reply;
import com.dpwgc.muranapi.Request;
import com.dpwgc.muranapi.Router;

import java.io.IOException;
import java.util.Arrays;

public class Demo {

    public static void main(String[] args) throws IOException {
        DemoController demo = new DemoController();
        new Router()
                .get("/hello", demo::hello)
                .post("/submit", demo::submit)
                .error(System.out::println)
                .port(8001)
                // use https
                // .server(HttpsServer.create(new InetSocketAddress(442), 0))
                .run();
    }

    public static class DemoController {

        public Reply hello(Request request) {
            System.out.println(request.getHeaders().map());
            System.out.println(request.getQuery().map());
            return new Reply("hi");
        }

        public Reply submit(Request request) {
            System.out.println(Arrays.toString(request.getBody()));
            return new Reply("ok", new Params().set("Response-Header", "demo"));
        }
    }
}
