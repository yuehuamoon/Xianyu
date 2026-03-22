package commontrade.dealtrade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(
        basePackages = {"commontrade.usertrade.mapper", "commontrade.itemtrade.mapper", "commontrade.dealtrade.mapper"}, // 要扫描的Mapper包路径（数组形式）
        sqlSessionFactoryRef = "sqlSessionFactory" // 自定义SqlSessionFactory的bean名称（可选，默认无需指定）
)
public class DealTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DealTradeApplication.class, args);
    }

}
