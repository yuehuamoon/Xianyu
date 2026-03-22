package commontrade.managetrade.service;

import commontrade.commonyh.pojo.dto.ManageDTO;
import commontrade.commonyh.pojo.dto.ManageDTO;
import commontrade.commonyh.pojo.entity.Good;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ManageService {
    List<ManageDTO> selectAll(int page);
    ManageDTO selectByID(ManageDTO manage);
    List<ManageDTO> selectByBuyerID(ManageDTO manage);
    List<ManageDTO> selectBySellerID(ManageDTO manage);
    ManageDTO selectByGoodID(ManageDTO manage);
    int InsertGood(ManageDTO manage);

    int deleteByID(ManageDTO good);
    int updateManageSelective(ManageDTO good);
    int buyerToAdmin(ManageDTO manage);
    int adminToSeller(ManageDTO manage);
    int balancePay(ManageDTO manage, String type);
    int wechatPay(ManageDTO manage);
    int aliPay(ManageDTO manage);
}
