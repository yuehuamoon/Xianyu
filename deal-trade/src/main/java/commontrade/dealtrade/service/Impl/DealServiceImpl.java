package commontrade.dealtrade.service.Impl;

import commontrade.commonyh.pojo.dto.DealDTO;
import commontrade.commonyh.pojo.dto.GoodDTO;
import commontrade.commonyh.pojo.entity.Good;
import commontrade.commonyh.pojo.entity.User;
import commontrade.commonyh.utils.TotalPrice;
import commontrade.dealtrade.mapper.DealMapper;
import commontrade.dealtrade.service.DealService;
import commontrade.itemtrade.mapper.ItemMapper;
import commontrade.usertrade.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class DealServiceImpl implements DealService {

    @Autowired
    private DealMapper dealMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public List<DealDTO> selectAll(int page) {
        return dealMapper.selectAll(page);
    }

    @Override
    public DealDTO selectByID(DealDTO deal) {
        return dealMapper.selectByID(deal);
    }

    @Override
    public List<DealDTO> selectByBuyerID(DealDTO deal) {
        return dealMapper.selectByBuyID(deal.getBuyerId());
    }

    @Override
    public List<DealDTO> selectBySellerID(DealDTO deal) {
        return dealMapper.selectBySellerID(deal.getSellerId());
    }

    @Override
    public DealDTO selectByGoodID(DealDTO deal) {
        return dealMapper.selectByGoodID(deal.getGoodId());
    }

    @Override
    public int InsertGood(DealDTO deal) {
        return dealMapper.insertDeal(deal);
    }

    @Override
    public int deleteByID(DealDTO good) {
        return dealMapper.deleteByID(good.getId());
    }

    @Override
    public int updateDealSelective(DealDTO good) {
        return dealMapper.updateDealSelective(good);
    }


    @Override
    public int buyerToAdmin(DealDTO deal) {
        if (deal.getStatus().equals(0)) {
            User userBuyer = userMapper.selectByID(deal.getBuyerId());
            User userAdmin = userMapper.selectByID(16);
            // 交易模块中计算订单总价（错误写法）
            Integer count = deal.getCount(); // 购买数量（Integer）
            BigDecimal unitPrice = deal.getUnitPrice(); // 单价（BigDecimal）
            BigDecimal totalPrice = TotalPrice.calculateTotalPrice(count, unitPrice);
            try  {
                if (Objects.nonNull(userBuyer) && Objects.nonNull(userAdmin)) {
                    userBuyer.setWallet(userAdmin.getWallet().add(totalPrice));
                    userAdmin.setWallet(userBuyer.getWallet().subtract(totalPrice));
                    deal.setStatus(1);
                    dealMapper.updateDealSelective(deal);
                }
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public int adminToSeller(DealDTO deal) {
        if (deal.getStatus().equals(0)) {
            User userAdmin = userMapper.selectByID(16);
            User userSeller = userMapper.selectByID(deal.getSellerId());
            // 交易模块中计算订单总价（错误写法）
            Integer count = deal.getCount(); // 购买数量（Integer）
            BigDecimal unitPrice = deal.getUnitPrice(); // 单价（BigDecimal）
            BigDecimal totalPrice = TotalPrice.calculateTotalPrice(count, unitPrice);
            try  {
                if (Objects.nonNull(userSeller) && Objects.nonNull(userAdmin)) {
                    userAdmin.setWallet(userAdmin.getWallet().subtract(totalPrice));
                    userSeller.setWallet(userSeller.getWallet().add(totalPrice));
                    deal.setStatus(1);
                    dealMapper.updateDealSelective(deal);
                }
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }


    @Override
    public  int balancePay(DealDTO deal, String type) {
        if (Objects.equals(type, "buyerToAdmin")) {
            return buyerToAdmin(deal);
        } else if (Objects.equals(type, "adminToSeller")) {
            return adminToSeller(deal);
        } else {
            return 0;
        }
    }

    @Override
    public int wechatPay(DealDTO deal) {
        return 0;
    }

    @Override
    public int aliPay(DealDTO deal) {
        return 0;
    }

    // 统计方法实现
    @Override
    public int countAll() {
        return dealMapper.countAll();
    }

    @Override
    public int countByStatus(int status) {
        return dealMapper.countByStatus(status);
    }

    @Override
    public Long sumCompletedAmount() {
        return dealMapper.sumCompletedAmount();
    }


}
