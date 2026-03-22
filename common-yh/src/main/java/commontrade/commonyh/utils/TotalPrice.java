package commontrade.commonyh.utils;

import java.math.BigDecimal;
import java.util.Objects;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 交易模块金额计算工具类
 * 封装订单总价、退款金额等核心计算逻辑
 */
public class TotalPrice {

    /**
     * 封装：计算订单总价（购买数量 × 单价）
     * @param count 购买数量（Integer，允许null，默认值1）
     * @param unitPrice 商品单价（BigDecimal，不允许null）
     * @return 订单总价（BigDecimal，高精度无精度丢失）
     * @throws IllegalArgumentException 单价为null时抛出异常
     */
    public static BigDecimal calculateTotalPrice(Integer count, BigDecimal unitPrice) {
        // 1. 必传参数校验：单价不能为空（金额计算核心字段）
        if (Objects.isNull(unitPrice)) {
            throw new IllegalArgumentException("商品单价[unitPrice]不能为空，无法计算订单总价");
        }

        // 2. 空值处理：购买数量为null时，使用默认值1（匹配deal表count字段默认值）
        int countValue = Objects.isNull(count) ? 1 : count;

        // 3. Integer转BigDecimal，避免类型不兼容问题
        BigDecimal countBigDecimal = new BigDecimal(countValue);

        // 4. 高精度乘法计算总价（BigDecimal不可变，需接收返回值）
        return countBigDecimal.multiply(unitPrice);
    }

    // ========== 扩展：如果需要非静态方法（比如依赖其他服务），可这样写 ==========
    /*
    // 非静态方法（需实例化工具类使用）
    public BigDecimal calculateTotalPriceNonStatic(Integer count, BigDecimal unitPrice) {
        // 逻辑和静态方法一致，仅去掉static
        if (Objects.isNull(unitPrice)) {
            throw new IllegalArgumentException("商品单价[unitPrice]不能为空");
        }
        int countValue = Objects.isNull(count) ? 1 : count;
        return new BigDecimal(countValue).multiply(unitPrice);
    }
    */
}