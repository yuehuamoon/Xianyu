package commontrade.managetrade.service.Impl;

import commontrade.commonyh.pojo.dto.ManageDTO;
import commontrade.commonyh.pojo.dto.GoodDTO;
import commontrade.commonyh.pojo.entity.Good;
import commontrade.commonyh.pojo.entity.User;
import commontrade.commonyh.utils.TotalPrice;
import commontrade.managetrade.mapper.ManageMapper;
import commontrade.managetrade.service.ManageService;
import commontrade.itemtrade.mapper.ItemMapper;
import commontrade.usertrade.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    private ManageMapper manageMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public List<ManageDTO> selectAll(int page) {
        return manageMapper.selectAll(page);
    }

    @Override
    public ManageDTO selectByID(ManageDTO manage) {
        return manageMapper.selectByID(manage);
    }

    @Override
    public List<ManageDTO> selectByBuyerID(ManageDTO manage) {
        return manageMapper.selectByBuyID(manage.getBuyerId());
    }

    @Override
    public List<ManageDTO> selectBySellerID(ManageDTO manage) {
        return manageMapper.selectBySellerID(manage.getSellerId());
    }

    @Override
    public ManageDTO selectByGoodID(ManageDTO manage) {
        return manageMapper.selectByGoodID(manage.getGoodId());
    }

    @Override
    public int InsertGood(ManageDTO manage) {
        return manageMapper.insertManage(manage);
    }

    @Override
    public int deleteByID(ManageDTO good) {
        return manageMapper.deleteByID(good.getId());
    }

    @Override
    public int updateManageSelective(ManageDTO good) {
        return manageMapper.updateManageSelective(good);
    }


    @Override
    public int buyerToAdmin(ManageDTO manage) {
        if (manage.getStatus().equals(0)) {
            User userBuyer = userMapper.selectByID(manage.getBuyerId());
            User userAdmin = userMapper.selectByID(16);
            // 交易模块中计算订单总价（错误写法）
            Integer count = manage.getCount(); // 购买数量（Integer）
            BigDecimal unitPrice = manage.getUnitPrice(); // 单价（BigDecimal）
            BigDecimal totalPrice = TotalPrice.calculateTotalPrice(count, unitPrice);
            try  {
                if (Objects.nonNull(userBuyer) && Objects.nonNull(userAdmin)) {
                    userBuyer.setWallet(userAdmin.getWallet().add(totalPrice));
                    userAdmin.setWallet(userBuyer.getWallet().subtract(totalPrice));
                    manage.setStatus(1);
                    manageMapper.updateManageSelective(manage);
                }
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public int adminToSeller(ManageDTO manage) {
        if (manage.getStatus().equals(0)) {
            User userAdmin = userMapper.selectByID(16);
            User userSeller = userMapper.selectByID(manage.getSellerId());
            // 交易模块中计算订单总价（错误写法）
            Integer count = manage.getCount(); // 购买数量（Integer）
            BigDecimal unitPrice = manage.getUnitPrice(); // 单价（BigDecimal）
            BigDecimal totalPrice = TotalPrice.calculateTotalPrice(count, unitPrice);
            try  {
                if (Objects.nonNull(userSeller) && Objects.nonNull(userAdmin)) {
                    userAdmin.setWallet(userAdmin.getWallet().subtract(totalPrice));
                    userSeller.setWallet(userSeller.getWallet().add(totalPrice));
                    manage.setStatus(1);
                    manageMapper.updateManageSelective(manage);
                }
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }


    @Override
    public  int balancePay(ManageDTO manage, String type) {
        if (Objects.equals(type, "buyerToAdmin")) {
            return buyerToAdmin(manage);
        } else if (Objects.equals(type, "adminToSeller")) {
            return adminToSeller(manage);
        } else {
            return 0;
        }
    }

    @Override
    public int wechatPay(ManageDTO manage) {
        return 0;
    }

    @Override
    public int aliPay(ManageDTO manage) {
        return 0;
    }


}
