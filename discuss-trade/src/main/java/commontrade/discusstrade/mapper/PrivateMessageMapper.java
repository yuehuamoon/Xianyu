package commontrade.discusstrade.mapper;

import commontrade.discusstrade.entity.PrivateMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 私聊消息Mapper接口
 */
@Mapper
public interface PrivateMessageMapper {

    /**
     * 插入消息
     */
    int insert(PrivateMessage message);

    /**
     * 根据ID查询
     */
    PrivateMessage selectById(@Param("id") Long id);

    /**
     * 查询两个用户之间的聊天记录
     */
    List<PrivateMessage> selectChatHistory(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    /**
     * 查询用户未读消息
     */
    List<PrivateMessage> selectUnreadMessages(@Param("userId") Long userId);

    /**
     * 查询用户未读消息数量
     */
    Long countUnreadMessages(@Param("userId") Long userId);

    /**
     * 标记消息为已读
     */
    int updateStatusToRead(@Param("id") Long id);

    /**
     * 标记用户所有未读消息为已读
     */
    int updateAllStatusToRead(@Param("userId") Long userId);

    /**
     * 查询用户最近联系人
     */
    List<Long> selectRecentContacts(@Param("userId") Long userId);
}