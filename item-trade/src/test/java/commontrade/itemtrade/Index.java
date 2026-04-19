package commontrade.itemtrade;

import lombok.Data;

/**
 * 对应 my-index 索引的 _source 字段
 */
@Data
public class Index {
    private String id;
    private String title;
    private String description;
}