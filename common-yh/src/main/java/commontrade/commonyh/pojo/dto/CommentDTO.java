package commontrade.commonyh.pojo.dto;

import lombok.Data;
@Data
public class CommentDTO {
    private String type;   // good, user, post
    private Integer id;    // 评论ID
    private Integer userId;
    private String content;
    private String userName;
    private String goodId;
    private String goodName;
    private Integer attitude;
    private Integer postId;
    private Integer toUserId;
    private String toUserName;
}