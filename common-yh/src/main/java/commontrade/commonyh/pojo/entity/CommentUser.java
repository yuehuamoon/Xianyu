package commontrade.commonyh.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentUser {
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer toUserId;
    private String toUserName;
    private Integer attitude;
    private String content;
    private Integer deleteStatu;
    private Integer status;
    private Integer reviewStatu;
    private Integer likes;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;
    private String ip;
}