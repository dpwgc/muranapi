import com.dpwgc.muranapi.Reply;
import com.dpwgc.muranapi.Router;

import java.io.IOException;

public class Demo {

    public static void main(String[] args) throws IOException {
        new Router().get("/hello", (request) -> {
            System.out.println(request.getHeaders().source());
            System.out.println(request.getQuery().source());
            return new Reply("hi".getBytes());
        }).port(8001).error((System.out::println)).run();
    }
}
