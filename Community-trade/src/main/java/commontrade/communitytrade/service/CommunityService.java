package commontrade.communitytrade.service;

import commontrade.commonyh.pojo.dto.GoodDTO;
import commontrade.commonyh.pojo.dto.LoginDTO;
import commontrade.commonyh.pojo.entity.Good;
import commontrade.commonyh.pojo.entity.User;
import commontrade.commonyh.pojo.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {
    List<Good> selectAll(int page);
    GoodDTO selectByID(int id);
    List<GoodDTO> selectBySellerId(int id);
    List<GoodDTO> selectByLabel(String label);
    List<GoodDTO> search(String keyword);
    int InsertGood(GoodDTO good);

    int deleteByID(GoodDTO good);
    int updateItemSelective(GoodDTO good);
    int updateGood(int starus, int id);
}
