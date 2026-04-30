package commontrade.recommendtrade.controller;



import commontrade.commonyh.pojo.entity.Good;
import commontrade.commonyh.pojo.result.Result;
import commontrade.recommendtrade.service.RecommendService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Slf4j
public class RecommondController {

    @Autowired
    private RecommendService recommendService;

    @GetMapping("/recommend")
    public Result<List<Good>> recommend(@RequestParam Integer userId) {
        log.info("推荐请求，用户ID：{}", userId);
        List<Good> goods = recommendService.selectGoodsByIds(userId);
        return Result.success(goods);
    }
}
