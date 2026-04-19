package commontrade.commonyh.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    private Integer id;
    private Integer userId;
    private Integer targetId;
    private Integer type;
    private Integer status;
    private LocalDateTime updateTime;
    private String ip;
}