package commontrade.dealtrade.service;

import commontrade.commonyh.pojo.dto.DealDTO;
import commontrade.commonyh.pojo.dto.DealDTO;
import commontrade.commonyh.pojo.entity.Good;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DealService {
    List<DealDTO> selectAll(int page);
    DealDTO selectByID(DealDTO deal);
    List<DealDTO> selectByBuyerID(DealDTO deal);
    List<DealDTO> selectBySellerID(DealDTO deal);
    DealDTO selectByGoodID(DealDTO deal);
    int InsertGood(DealDTO deal);

    int deleteByID(DealDTO good);
    int updateDealSelective(DealDTO good);
    int buyerToAdmin(DealDTO deal);
    int adminToSeller(DealDTO deal);
    int balancePay(DealDTO deal, String type);
    int wechatPay(DealDTO deal);
    int aliPay(DealDTO deal);

    // 统计方法
    int countAll();
    int countByStatus(int status);
    Long sumCompletedAmount();
}
