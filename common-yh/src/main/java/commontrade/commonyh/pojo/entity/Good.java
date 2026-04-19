package commontrade.commonyh.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.util.Date;

/**
 * 校园二手交易-商品表实体类
 * 对应ES索引：good
 */
@Data
@Document(indexName = "good")
public class Good {

    // ===================== 修复：静态常量删除 @Field 注解 =====================
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID，自增主键
     * 修复：Integer 对应 FieldType.Long
     */
    @Id
    @Field(type = FieldType.Long)
    private Integer id;

    @Field(type = FieldType.Integer)
    private Integer sellerId;

    /**
     * 物品名
     */
    @Field(type = FieldType.Text)
    private String name;

    /**
     * 物品描述
     */
    @Field(type = FieldType.Text)
    private String content;

    /**
     * 物品单价（单位：分）
     */
    @Field(type = FieldType.Integer)
    private Integer unitPrice;

    /**
     * 物品数量
     */
    @Field(type = FieldType.Integer)
    private Integer count = 1;

    /**
     * 图片链接
     */
    @Field(type = FieldType.Text)
    private String imgUrl;

    /**
     * 标签
     */
    @Field(type = FieldType.Text)
    private String label;

    /**
     * 状态：0-审核中，1-上架，2-售出，3-失效
     */
    @Field(type = FieldType.Integer)
    private Byte status = 0;

    /**
     * 交易地址
     */
    @Field(type = FieldType.Text)
    private String address;

    /**
     * 浏览量
     */
    @Field(type = FieldType.Integer)
    private Integer viewCount = 0;

    /**
     * 收藏量
     */
    @Field(type = FieldType.Integer)
    private Integer favoriteCount = 0;

    /**
     * 创建时间
     * 修复：指定 ES 日期格式
     */
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 删除时间（软删除）
     */
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deleteTime;
}