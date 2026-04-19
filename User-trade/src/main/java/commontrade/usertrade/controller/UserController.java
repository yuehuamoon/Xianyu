package commontrade.usertrade.controller;


import com.example.demo.utils.JwtUtil;
import commontrade.commonyh.pojo.dto.EmailCode;
import commontrade.commonyh.pojo.dto.LoginDTO;
import commontrade.commonyh.pojo.dto.Page;
import commontrade.commonyh.pojo.entity.User;
import commontrade.commonyh.pojo.enums.RoleEnum;
import commontrade.commonyh.pojo.result.Result;
import commontrade.commonyh.pojo.vo.UserVO;
import commontrade.usertrade.email.EmailService;
import commontrade.usertrade.service.UserService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*") // 允许前端开发服务器访问
@Slf4j
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/page")
    public Result<List<User>> ShowUser(@RequestBody Page page) {
        return Result.success(userService.selectByPage((page.getPage()-1)*10));
    };


    @PostMapping("/generateEmailCode")
    public Result genateEmailCode(@RequestBody UserVO userVO) throws MessagingException {
        try {
            emailService.sendCode(userVO.getEmail());
        }  catch (MessagingException e) {
            System.err.println(e.getMessage());
            return Result.error(e.getMessage());
        }
        return Result.success("success");
    };

    @PostMapping("/checkEmailCode")
    public Result checkEmailCode(@RequestBody EmailCode emailCode) throws MessagingException {
        boolean statu = emailService.checkEmailCode(emailCode);
        System.out.println(statu);
        if (statu) {
            return Result.success("success");
        }
        return Result.error("error");
    }
    @PostMapping("/selectById")
    public Result<UserVO> selectById(@RequestBody UserVO user) {
        return Result.success(userService.selectById(user.getId()));
    }
    @GetMapping("/get")
    public Result<List<User>> ShowUser() {
        return Result.success(userService.selectAll());
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO user) {
        // 1. 校验用户名密码（省略真实逻辑）
        UserVO userVO = userService.login(user);
        if (userService.login(user) != null) {
            // 2. 构建JWT载荷
            Map claims = new HashMap<>();
            claims.put("userId", userVO.getId());
            claims.put("phone", user.getPhone());
            claims.put("role", userVO.getRole());
            claims.put("address", userVO.getAddress());
            claims.put("gender", userVO.getGender());
            claims.put("nickName", userVO.getNickName());
            claims.put("email",  userVO.getEmail());

            // 3. 生成Token
            return Result.success(jwtUtil.genJwt(claims));

        }
        System.out.println(user.getPhone());
        return Result.error("mi ma cuo wu");
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserVO userVO) {
        try {
            // 调用服务层方法
            EmailCode emailCode = new EmailCode();
            emailCode.setEmail(userVO.getEmail());
            emailCode.setCode(userVO.getCode());
            if  (!emailService.checkEmailCode(emailCode)) {
                return Result.error("验证码错误");
            }
            int result = userService.registerUser(userVO);
            if (result == 1) {
                return Result.success("注册成功");
            } else {
                return Result.error(400, "注册失败：数据库插入无影响行数");
            }
        } catch (Exception e) {
            // 1. 打印完整的异常堆栈（关键！）
            log.error("用户注册接口异常", e);
            // 2. 处理 message 为空的情况
            String errorMsg = e.getMessage() == null ? e.getClass().getName() : e.getMessage();
            // 3. 返回详细的错误信息（包含异常类型）
            return Result.error(500, "注册失败：" + errorMsg);
        }
    }

    @PostMapping("/delete")
    public Result deleteUser(@RequestBody UserVO user) {

        if (user.getPhone() != null) {
            int msg= userService.deleteByPhone(user);
            return Result.success(msg);
        }
        return Result.error("wei deng lu");
    }

    @PostMapping("/update")
    public Result updateUser(@RequestBody User user) {
        System.out.println(user);
        if (user.getPhone() != null) {
            int msg = userService.updateUserSelective(user);
            return Result.success(msg);
        }
        return Result.error("wei deng lu");
    }

    @PostMapping("/identify")
    public Result identify(@RequestBody User user) {
        if (user.getPhone() != null) {
            return Result.success(userService.identifyByAdmin(user.getStatus(), user.getPhone()));
        }
        return Result.error("have not phone");
    }

    @GetMapping("/info")
    public Result<UserVO> info(UserVO userVO) {
        return Result.success(userService.selectByPhone(userVO.getPhone()));
    }

    // 统计接口
    @GetMapping("/count")
    public Result<Map<String, Object>> count() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", userService.countAll());
        stats.put("active", userService.countByStatus(1));  // status=1 正常
        stats.put("banned", userService.countByStatus(0));  // status=0 封禁
        stats.put("admin", userService.countByRole(1));     // role=1 管理员
        stats.put("normal", userService.countByRole(2));    // role=2 普通用户
        return Result.success(stats);
    }

}
