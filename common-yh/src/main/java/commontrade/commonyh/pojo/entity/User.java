package commontrade.commonyh.pojo.entity;

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
public class User {

    /**
     * 主键ID，自动递增
     */
    private Integer id;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 钱包余额（使用BigDecimal避免浮点精度问题）
     */
    private BigDecimal wallet;

    /**
     * 身份：0-普通用户，1-管理员，2-其他
     */
    private Integer role;

    /**
     * 电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 性别：0-男，1-女
     */
    private Integer gender;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 证件号码
     */
    private String certNo;

    /**
     * 状态：0-封禁，1-正常，2-审核中
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除时间（默认为null）
     */
    private Date deleteTime;
}