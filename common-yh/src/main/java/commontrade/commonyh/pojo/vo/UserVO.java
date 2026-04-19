package commontrade.commonyh.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户实体类
 * 对应数据库 user 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private Integer id;
    private String realName;
    private String nickName;
    private String password;
    private Double wallet; // 注意类型：金额推荐用Double/Decimal
    private String role;
    private String phone;
    private String email;
    private String address;
    private String gender; // 性别：0=未知，1=男，2=女
    private String avatarUrl;
    private String certNo;
    private Integer status; // 状态：1=启用
    private String code;
}