package commontrade.itemtrade.service;

import commontrade.commonyh.pojo.dto.GoodDTO;
import commontrade.commonyh.pojo.dto.LoginDTO;
import commontrade.commonyh.pojo.entity.Good;
import commontrade.commonyh.pojo.entity.User;
import commontrade.commonyh.pojo.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ItemService {
    List<Good> selectAll(int page);
    List<Good> selectAllGood();
    GoodDTO selectByID(int id);
    List<GoodDTO> selectBySellerId(int id);
    List<GoodDTO> selectByLabel(String label);
    List<GoodDTO> search(String keyword);
    int InsertGood(GoodDTO good);

    int deleteByID(GoodDTO good);
    int updateItemSelective(GoodDTO good);
    int updateGood(int starus, int id);

    // 统计方法
    int countAll();
    int countByStatus(int status);
    List<Map<String, Object>> countGroupByLabel();
}
