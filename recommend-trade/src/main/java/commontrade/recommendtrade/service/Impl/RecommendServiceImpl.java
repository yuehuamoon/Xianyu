package commontrade.recommendtrade.service.Impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import commontrade.commonyh.pojo.entity.Good;
import commontrade.recommendtrade.mapper.RecommendMapper;
import commontrade.recommendtrade.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RecommendMapper recommendMapper;

    /**
     * 获取推荐商品ID列表（只返回 itemId）
     */
    public List<Good> selectGoodsByIds(Integer userId) {
        String url = "http://127.0.0.1:5000/recommend";
        Map<String, Object> params = Map.of("user_id", userId, "top_n", 10);

        // 1. 请求接口
        String res = restTemplate.postForObject(url, params, String.class);

        // 2. 解析JSON
        Map<String, Object> result = JSON.parseObject(res, new TypeReference<>() {});
        List<List<Number>> data = (List<List<Number>>) result.get("data");

        // 3. 只提取第一个数字：商品ID
        List<Long> selectData = data.stream()
                .map(item -> item.get(0).longValue())
                .toList();

        return recommendMapper.selectGoodsByIds(selectData);
    }
}
