package commontrade.dealtrade.controller;


import commontrade.commonyh.pojo.dto.GoodDTO;
import commontrade.dealtrade.service.DealService;
import commontrade.dealtrade.utils.JwtUtil;
import commontrade.commonyh.pojo.dto.Page;
import commontrade.commonyh.pojo.entity.Good;
import commontrade.commonyh.pojo.result.Result;
import commontrade.commonyh.pojo.dto.DealDTO;
import commontrade.itemtrade.mapper.ItemMapper;
import commontrade.itemtrade.service.ItemService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
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

}
