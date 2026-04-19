package commontrade.itemtrade;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import commontrade.commonyh.pojo.entity.Good;
import commontrade.itemtrade.mapper.ItemMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.List;

@SpringBootTest
class ItemTradeApplicationTests {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Autowired
    private ItemMapper itemMapper;
    @Test
    void showIndexData() {
        // 1. 指定索引
        IndexCoordinates index = IndexCoordinates.of("good");
        // 2. 查询全部数据
        List<SearchHit<Object>> searchHits = elasticsearchOperations.search(
                Query.findAll(),
                Object.class,
                index
        ).getSearchHits();

        // 3. 直接打印数据（最简单、不序列化、不报错）
        System.out.println("==================== my-index 索引信息 ====================");
        System.out.println("总数据量：" + searchHits.size());
        System.out.println("============================================");

        // 遍历打印每一条文档
        for (SearchHit<Object> hit : searchHits) {
            System.out.println("索引：" + hit.getIndex());
            System.out.println("文档ID：" + hit.getId());
            System.out.println("文档内容：" + hit.getContent());
            System.out.println("----------------------------------------");
        }
    }

    @Test
    public void importMysqlToEs() {
        // 1. MyBatis 查询所有商品数据
        List<Good> goodList = itemMapper.selectAll(1);
        if (goodList.isEmpty()) {
            System.out.println("MySQL中无商品数据！");
            return;
        }

        // 2. 指定ES索引：good（和你实体类@Document(indexName = "good")一致）
        IndexCoordinates index = IndexCoordinates.of("good");

        // 3. 批量写入ES（高效、无报错）
        for (Good good : goodList) {
            elasticsearchOperations.save(good, index);
        }

        System.out.println("导入成功！MyBatis读取数据：" + goodList.size() + " 条，已全部导入ES good索引");
    }


    @Test
    void searchByKeyword() {
        // 索引
        IndexCoordinates index = IndexCoordinates.of("good");

        // 🔥 搜索关键词：你要搜的中文
        String keyword = "重记";

        // 构建查询：匹配 name 字段（自动用IK分词）
        Criteria criteria = Criteria.where("name").matches(keyword)
                .or("content").matches(keyword)
                .or("label").matches(keyword);

        Query query = new CriteriaQuery(criteria);

        // 执行搜索
        List<SearchHit<Object>> searchHits = elasticsearchOperations.search(
                query,
                Object.class,
                index
        ).getSearchHits();

        // 输出结果
        System.out.println("搜索到：" + searchHits.size() + " 条数据");
        for (SearchHit<Object> hit : searchHits) {
            System.out.println(hit.getContent());
        }
    }
    }




