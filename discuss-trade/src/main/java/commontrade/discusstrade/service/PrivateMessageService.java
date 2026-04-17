package commontrade.discusstrade.service;

import commontrade.discusstrade.entity.PrivateMessage;
import commontrade.discusstrade.mapper.PrivateMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 私聊消息服务层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateMessageService {

    private final PrivateMessageMapper privateMessageMapper;

    /**
     * 保存私聊消息
     */
    @Transactional
    public PrivateMessage saveMessage(Long fromUserId, Long toUserId, String content) {
        PrivateMessage message = new PrivateMessage();
        message.setFromUserId(fromUserId);
        message.setToUserId(toUserId);
        message.setContent(content);
        message.setStatus(0);
        message.setCreateTime(LocalDateTime.now());
        privateMessageMapper.insert(message);
        return message;
    }

    /**
     * 获取两个用户之间的聊天记录
     */
    public List<PrivateMessage> getChatHistory(Long userId1, Long userId2) {
        return privateMessageMapper.selectChatHistory(userId1, userId2);
    }

    /**
     * 获取用户未读消息
     */
    public List<PrivateMessage> getUnreadMessages(Long userId) {
        return privateMessageMapper.selectUnreadMessages(userId);
    }

    /**
     * 获取用户未读消息数量
     */
    public Long getUnreadCount(Long userId) {
        return privateMessageMapper.countUnreadMessages(userId);
    }

    /**
     * 标记消息为已读
     */
    @Transactional
    public void markAsRead(Long messageId) {
        privateMessageMapper.updateStatusToRead(messageId);
    }

    /**
     * 标记用户所有未读消息为已读
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        privateMessageMapper.updateAllStatusToRead(userId);
    }

    /**
     * 获取用户最近联系人
     */
    public List<Long> getRecentContacts(Long userId) {
        return privateMessageMapper.selectRecentContacts(userId);
    }
}