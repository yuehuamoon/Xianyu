package commontrade.es.esMapper;

import commontrade.commonyh.pojo.entity.Good;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public class ItemESMapper {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public List<Good> searchByKeyword(String keyword, String table) {
        IndexCoordinates index = IndexCoordinates.of(table);
        System.out.println(keyword+table);

        // 构建查询：匹配 name 字段（自动用IK分词）
        Criteria criteria = Criteria.where("name").matches(keyword)
                .or("content").matches(keyword)
                .or("label").matches(keyword);

        Query query = new CriteriaQuery(criteria);

        // 执行搜索
        List<Good> searchHits = elasticsearchOperations.search(
                query,
                Good.class,
                index
        ).getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();
        System.out.println(searchHits);

        return  searchHits;
    }

}
