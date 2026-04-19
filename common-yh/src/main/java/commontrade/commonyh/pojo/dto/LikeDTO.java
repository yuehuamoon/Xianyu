package commontrade.commonyh.pojo.dto;

import lombok.Data;
@Data
public class LikeDTO {
    private Integer userId;
    private Integer targetId;
    private Integer type;  // 1、2、3
    private String ip;
}