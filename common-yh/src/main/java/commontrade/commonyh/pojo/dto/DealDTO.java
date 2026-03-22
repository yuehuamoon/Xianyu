package commontrade.commonyh.pojo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易订单实体类
 * 对应数据库表：deal
 * 接口前缀：/deal，端口：8084
 */
@Data // 自动生成getter、setter、toString、equals、hashCode、无参构造
@AllArgsConstructor
@NoArgsConstructor
public class DealDTO {

    /**
     * 订单主键ID
     */
    private Integer id;

    /**
     * 卖出者ID
     */
    private Integer sellerId;

    /**
     * 购买者ID
     */
    private Integer buyerId;

    /**
     * 商品ID
     */
    private Integer goodId;

    /**
     * 单价（使用BigDecimal避免浮点精度问题）
     */
    private BigDecimal unitPrice;

    /**
     * 购买数量，默认1
     */
    private Integer count = 1;

    /**
     * 订单状态：0-待付款，1-已付款，2-已取消，3-已完成，4-已退款
     */
    private Integer status;

    /**
     * 订单创建时间，默认当前时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间，默认创建时间，更新时自动刷新
     */
    private LocalDateTime updateTime;

    // 可选：订单状态枚举（推荐添加，避免魔法值）
    public enum DealStatus {
        PENDING_PAYMENT(0, "待付款"),
        PAID(1, "已付款"),
        CANCELED(2, "已取消"),
        COMPLETED(3, "已完成"),
        REFUNDED(4, "已退款");

        private final Integer code;
        private final String desc;

        DealStatus(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}