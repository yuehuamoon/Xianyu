package commontrade.commonyh.pojo.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class GoodDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID，自增主键
     */
    private Integer id;


    private Integer sellerId;
    /**
     * 物品名
     */
    private String name;

    /**
     * 物品描述
     */
    private String content;

    /**
     * 物品单价（单位：分，避免小数精度问题）
     */
    private Integer unitPrice;

    /**
     * 物品数量，默认1
     */
    private Integer count = 1;

    /**
     * 物品图片链接，默认默认图
     */
    private String imgUrl;

    /**
     * 物品标签（如教材教辅/毕业季闲置）
     */
    private String label;

    /**
     * 物品状态：0-审核中，1-上架，2-售出，3-失效
     */
    private Byte status = 0;

    /**
     * 交易地址（如XX校区1号楼）
     */
    private String address;

    /**
     * 浏览量
     */
    private Integer viewCount = 0;

    /**
     * 收藏量
     */
    private Integer favoriteCount = 0;


}
