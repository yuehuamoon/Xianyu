package commontrade.discusstrade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 私聊消息响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateMessageResponse {
    
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
    
    /** 消息类型标识 */
    private String messageType;
    
    /** 是否发送成功 */
    private Boolean success;
    
    /** 提示信息 */
    private String message;
    
    /**
     * 创建成功响应
     */
    public static PrivateMessageResponse success(Long fromUserId, Long toUserId, String content) {
        return PrivateMessageResponse.builder()
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .content(content)
                .messageType("private")
                .success(true)
                .build();
    }
    
    /**
     * 创建失败响应
     */
    public static PrivateMessageResponse fail(String message) {
        return PrivateMessageResponse.builder()
                .success(false)
                .message(message)
                .build();
    }
}