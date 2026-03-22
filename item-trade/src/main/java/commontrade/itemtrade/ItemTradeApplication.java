package commontrade.itemtrade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "commontrade.itemtrade",
        "commontrade.commonyh",
})
public class ItemTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemTradeApplication.class, args);
    }

}
