package commontrade.discusstrade.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 私聊消息实体
 */
@Data
public class PrivateMessage {

    /** 消息ID */
    private Long id;

    /** 发送者用户ID */
    private Long fromUserId;

    /** 接收者用户ID */
    private Long toUserId;

    /** 消息内容 */
    private String content;

    /** 消息状态：0-已发送，1-已读 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 已读时间 */
    private LocalDateTime readTime;
}