package commontrade.commonyh.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Integer id;
    private String title;
    private String content;
    private Integer status;
    private Integer reviewStatu;
    private Integer userId;
    private String userName;
    private String imgUrls;
    private Integer likes;
    private Integer favorite;
    private String ip;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}