package commontrade.recommendtrade.service;

import commontrade.commonyh.pojo.dto.LoginDTO;
import commontrade.commonyh.pojo.entity.Good;
import commontrade.commonyh.pojo.entity.User;
import commontrade.commonyh.pojo.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecommendService {
    List<Good> selectGoodsByIds(Integer userId);
}
