package commontrade.discusstrade.dto;

import lombok.Data;

/**
 * 私聊消息请求DTO
 */
@Data
public class PrivateMessageRequest {
    
    /** 消息类型 */
    private String type;
    
    /** 目标用户ID */
    private Long toUserId;
    
    /** 消息内容 */
    private String content;
}