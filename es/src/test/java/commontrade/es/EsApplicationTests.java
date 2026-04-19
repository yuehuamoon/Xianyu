package commontrade.es;

import commontrade.commonyh.pojo.entity.Good;
import commontrade.es.esMapper.ItemESMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EsApplicationTests {

    @Autowired
    private ItemESMapper itemESMapper;
    @Test
    void contextLoads() {
        List<Good> goods = itemESMapper.searchByKeyword("毕业", "good");
    }

}
