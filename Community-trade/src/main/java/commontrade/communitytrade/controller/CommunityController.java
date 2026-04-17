package commontrade.communitytrade.controller;


import commontrade.commonyh.pojo.dto.DealDTO;
import commontrade.communitytrade.utils.JwtUtil;
import commontrade.commonyh.pojo.dto.Page;
import commontrade.commonyh.pojo.entity.Good;
import commontrade.commonyh.pojo.entity.User;
import commontrade.commonyh.pojo.result.Result;
import commontrade.commonyh.pojo.dto.GoodDTO;
import commontrade.itemtrade.mapper.ItemMapper;
import commontrade.itemtrade.service.ItemService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/good")
@CrossOrigin(origins = "http://localhost:5173") // 允许前端开发服务器访问
public class ItemController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ItemService itemService;

    @PostMapping("/info")
    public Result<List<Good>> ShowUser(@RequestBody Page page) {
        List<Good> goods = itemService.selectAll((page.getPage()-1)*10);

        return Result.success(goods);
    }

    @PostMapping("/goodID")
    public Result SelectByID(@RequestBody Page page) {
        return Result.success(itemService.selectByID(page.getPage()));
    }

    @PostMapping("/selectByUserId")
    public Result<List<GoodDTO>> SelectByUserId(@RequestBody User user) {
        return Result.success(itemService.selectBySellerId(user.getId()));
    }
    @PostMapping("/search")
    public Result search(@RequestBody GoodDTO goodDTO) {
        if (goodDTO.getLabel() != null && goodDTO.getLabel().trim().length()>0) {
            return Result.success(itemService.selectByLabel(goodDTO.getLabel()));
        }

        return Result.error("error");
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody GoodDTO goodDTO) {
        try {
            return Result.success(itemService.InsertGood(goodDTO));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

    }

    @PostMapping("/deleteByID")
    public Result deleteItemByID(@RequestBody GoodDTO goodDTO) {

        if (goodDTO.getId() != null) {
            int msg= itemService.deleteByID(goodDTO);
            return Result.success(msg);
        }
        return Result.error("wei deng lu");
    }

    @PostMapping("/update")
    public Result updateUser(@RequestBody GoodDTO goodDTO) {

        if (goodDTO.getSellerId() != null) {
            int msg = itemService.updateItemSelective(goodDTO);
            return Result.success(msg);
        }
        return Result.error("wei deng lu");
    }

    @PostMapping("/review/confirm")
    public Result confirm(@RequestBody GoodDTO goodDTO, @RequestHeader(value = "token", required = false) String token) {

        if (token == null && token.trim().length()>0) {
            return Result.error("token is null");
        }
        String jwtToken =token;
        if (token.startsWith("Bearer ")) {
            jwtToken = token.substring(7).trim();
        }
        Claims claims;
        try {
            claims = jwtUtil.parseJwt(jwtToken);
            if (claims.get("role").equals("admin")) {
                System.out.println(jwtToken);
            } else {
                return Result.error("role is not admin");
            }
        } catch (Exception e) {
            // 解析失败（Token过期/篡改/密钥错误等）
            return Result.error("Token无效或已过期：" + e.getMessage());
        }


        if (goodDTO.getId() != null) {
            return Result.success(itemService.updateGood(goodDTO.getStatus(), goodDTO.getId()));
        }
        return Result.error("have not phone");
    }

    @PostMapping("/review/info")
    public Result<List<Good>> info(@RequestBody Page page) {
        return Result.success(itemService.selectAll(page.getPage()));
    }

}
