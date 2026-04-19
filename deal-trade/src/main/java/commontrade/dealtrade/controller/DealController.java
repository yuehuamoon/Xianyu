package commontrade.dealtrade.controller;


import commontrade.commonyh.pojo.dto.GoodDTO;
import commontrade.commonyh.pojo.entity.Deal;
import commontrade.commonyh.pojo.entity.User;
import commontrade.commonyh.pojo.vo.DealVO;
import commontrade.commonyh.pojo.vo.UserVO;
import commontrade.dealtrade.service.DealService;
import commontrade.dealtrade.utils.JwtUtil;
import commontrade.commonyh.pojo.dto.Page;
import commontrade.commonyh.pojo.entity.Good;
import commontrade.commonyh.pojo.result.Result;
import commontrade.commonyh.pojo.dto.DealDTO;
import commontrade.itemtrade.mapper.ItemMapper;
import commontrade.itemtrade.service.ItemService;
import commontrade.usertrade.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/deal")
@CrossOrigin(origins = "http://localhost:5173") // 允许前端开发服务器访问
public class DealController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DealService dealService;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/info")
    public Result<List<DealDTO>> ShowUser(@RequestBody Page page) {
        List<DealDTO> deals = dealService.selectAll((page.getPage()-1)*10);
        return Result.success(deals);
    }
    @PostMapping("/selectByID")
    public Result<DealDTO> SelectByID(@RequestBody DealDTO deal) {
        DealDTO deals = dealService.selectByID(deal);
        return Result.success(deals);
    }
    @PostMapping("/selectBySellerID")
    public Result<List<DealVO>> SelectBySellerID(@RequestBody DealDTO deal) {
        // 1. 查询订单列表 【1次】
        List<DealDTO> deals = dealService.selectBySellerID(deal);
        // 空数据直接返回
        if (deals == null || deals.isEmpty()) {
            return Result.success(new ArrayList<>());
        }

        // ----------------------
        // 批量提取所有用户ID（去重）
        // ----------------------
        Set<Integer> userIds = new HashSet<>();
        for (DealDTO dto : deals) {
            userIds.add(dto.getBuyerId());
            userIds.add(dto.getSellerId());
        }

        // ----------------------
        // 批量一次查询所有用户 【1次】
        // ----------------------
        List<User> userList = userMapper.selectAll();

        // ----------------------
        // 转成 MAP 方便快速获取
        // ----------------------
        Map<Integer, User> userMap = new HashMap<>();
        for (User user : userList) {
            userMap.put(user.getId(), user);
        }

        // ----------------------
        // 组装 VO（内存操作，极快）
        // ----------------------
        List<DealVO> dealVOs = new ArrayList<>();
        for (DealDTO dto : deals) {

                DealVO vo = new DealVO();
                BeanUtils.copyProperties(dto, vo);
                GoodDTO goodDTO = itemMapper.selectById(dto.getGoodId());
                // 从map拿，不查库
                User buyer = userMap.get(dto.getBuyerId());
                User seller = userMap.get(dto.getSellerId());

                vo.setBuyerName(buyer != null ? buyer.getNickName() : "未知用户");
                vo.setSellerName(seller != null ? seller.getNickName() : "未知用户");
                vo.setGoodName(goodDTO.getName());
                dealVOs.add(vo);

        }

        return Result.success(dealVOs);
    }

    @PostMapping("/selectByBuyerID")
    public Result<List<DealVO>> SelectByBuyerID(@RequestBody DealDTO deal) {
        // 1. 查询订单列表 【1次】
        List<DealDTO> deals = dealService.selectByBuyerID(deal);

        // 空判断
        if (deals == null || deals.isEmpty()) {
            return Result.success(new ArrayList<>());
        }

        // 2. 提取所有需要的用户ID（买家 + 卖家）
        Set<Integer> userIds = new HashSet<>();
        for (DealDTO dto : deals) {
            userIds.add(dto.getBuyerId());
            userIds.add(dto.getSellerId());
        }

        // 3. 批量查询用户 【1次】
        List<User> userList = userMapper.selectAll();

        // 4. 转成Map，快速查找
        Map<Integer, User> userMap = new HashMap<>();
        for (User user : userList) {
            userMap.put(user.getId(), user);
        }

        // 5. 组装VO（批量赋值，不查库）
        List<DealVO> dealVOs = new ArrayList<>();
        for (DealDTO dto : deals) {
            try {
                DealVO vo = new DealVO();
                BeanUtils.copyProperties(dto, vo);
                GoodDTO goodDTO = itemMapper.selectById(dto.getGoodId());
                System.out.println(goodDTO);
                User buyer = userMap.get(dto.getBuyerId());
                User seller = userMap.get(dto.getSellerId());

                vo.setBuyerName(buyer != null ? buyer.getNickName() : "未知用户");
                vo.setSellerName(seller != null ? seller.getNickName() : "未知用户");
                vo.setGoodName(goodDTO.getName());
                dealVOs.add(vo);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return Result.success(dealVOs);
    }


    @PostMapping("/insert")
    public Result insert(@RequestBody DealDTO dealDTO) {

        GoodDTO goodDTO = itemMapper.selectById(dealDTO.getGoodId());
        if (goodDTO.getStatus().equals((byte) 2)) {
            return Result.error("已售出");
        }

        int good = itemMapper.updateGood(2, dealDTO.getGoodId());
        return Result.success(dealService.InsertGood(dealDTO));

    }

    @PostMapping("/deleteByID")
    public Result deleteDealByID(@RequestBody DealDTO dealDTO) {

        if (dealDTO.getId() != null && dealDTO.getStatus() != null) {
            int msg= dealService.deleteByID(dealDTO);
            System.out.println(msg);
            return Result.success(msg);
        }
        return Result.error("error");
    }

    @PostMapping("/update")
    public Result updateUser(@RequestBody DealDTO dealDTO) {

        if (dealDTO.getSellerId() != null) {
            int msg = dealService.updateDealSelective(dealDTO);
            return Result.success(msg);
        }
        return Result.error("wei deng lu");
    }

    @PostMapping("/create")
    public Result create(@RequestBody DealDTO dealDTO) {
        if (dealDTO.getSellerId() == null) {
            return Result.error("error");
        }

        dealService.balancePay(dealDTO, "adminToSeller");
        dealService.balancePay(dealDTO, "adminToSeller");

        return Result.success();
    }

    // 统计接口
    @GetMapping("/count")
    public Result<Map<String, Object>> count() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", dealService.countAll());
        stats.put("pending", dealService.countByStatus(0));   // status=0 待付款
        stats.put("paid", dealService.countByStatus(1));      // status=1 已付款
        stats.put("cancelled", dealService.countByStatus(2)); // status=2 已取消
        stats.put("completed", dealService.countByStatus(3)); // status=3 已完成
        stats.put("refunded", dealService.countByStatus(4));  // status=4 已退款
        stats.put("totalAmount", dealService.sumCompletedAmount()); // 已完成订单总金额
        return Result.success(stats);
    }

}
