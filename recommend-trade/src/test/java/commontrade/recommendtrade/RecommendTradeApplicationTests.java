package commontrade.recommendtrade;

import commontrade.commonyh.pojo.entity.Good;
import commontrade.recommendtrade.service.RecommendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class RecommendTradeApplicationTests {

    @Autowired
    RecommendService recommendService;

    @Test
    void contextLoads() {
    }

    @Test
    void RecommendTradeApplicationTest(){
        List<Good> goods = recommendService.selectGoodsByIds(12);

        System.out.println(goods);
    }


}
