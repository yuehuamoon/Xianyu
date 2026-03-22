package commontrade.managetrade.controller;


import commontrade.commonyh.pojo.dto.GoodDTO;
import commontrade.managetrade.service.ManageService;
import commontrade.managetrade.utils.JwtUtil;
import commontrade.commonyh.pojo.dto.Page;
import commontrade.commonyh.pojo.entity.Good;
import commontrade.commonyh.pojo.result.Result;
import commontrade.commonyh.pojo.dto.ManageDTO;
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
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ManageService manageService;
    @Autowired
    private ItemMapper itemMapper;

    @PostMapping("/info")
    public Result<List<ManageDTO>> ShowUser(@RequestBody Page page) {
        List<ManageDTO> manages = manageService.selectAll(page.getPage());
        return Result.success(manages);
    }
    @PostMapping("/selectByID")
    public Result<ManageDTO> SelectByID(@RequestBody ManageDTO manage) {
        ManageDTO manages = manageService.selectByID(manage);
        return Result.success(manages);
    }


    @PostMapping("/insert")
    public Result insert(@RequestBody ManageDTO manageDTO) {

        GoodDTO goodDTO = itemMapper.selectById(manageDTO.getGoodId());
        if (goodDTO.getStatus().equals((byte) 2)) {
            return Result.error("已售出");
        }

        int good = itemMapper.updateGood(2, manageDTO.getGoodId());
        return Result.success(manageService.InsertGood(manageDTO));

    }

    @PostMapping("/deleteByID")
    public Result deleteManageByID(@RequestBody ManageDTO manageDTO) {

        if (manageDTO.getId() != null && manageDTO.getStatus() != null) {
            int msg= manageService.deleteByID(manageDTO);
            return Result.success(msg);
        }
        return Result.error("error");
    }

    @PostMapping("/update")
    public Result updateUser(@RequestBody ManageDTO manageDTO) {

        if (manageDTO.getSellerId() != null) {
            int msg = manageService.updateManageSelective(manageDTO);
            return Result.success(msg);
        }
        return Result.error("wei deng lu");
    }

    @PostMapping("/create")
    public Result create(@RequestBody ManageDTO manageDTO) {
        if (manageDTO.getSellerId() == null) {
            return Result.error("error");
        }

        manageService.balancePay(manageDTO, "adminToSeller");
        manageService.balancePay(manageDTO, "adminToSeller");

        return Result.success();
    }

}
