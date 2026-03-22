package commontrade.usertrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "commontrade.usertrade", // 当前模块包
        "commontrade.commonyh",    // JwtUtil所在的跨模块包
        "com.example.demo.utils",
})
public class UserTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserTradeApplication.class, args);
    }

}
