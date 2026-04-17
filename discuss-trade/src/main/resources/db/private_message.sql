-- ==============================================
-- 私聊消息表
-- ==============================================

CREATE TABLE IF NOT EXISTS `private_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `from_user_id` BIGINT NOT NULL COMMENT '发送者用户ID',
    `to_user_id` BIGINT NOT NULL COMMENT '接收者用户ID',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '消息状态：0-已发送，1-已读',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `read_time` DATETIME DEFAULT NULL COMMENT '已读时间',
    PRIMARY KEY (`id`),
    INDEX `idx_from_user_id` (`from_user_id`),
    INDEX `idx_to_user_id` (`to_user_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='私聊消息表';

-- ==============================================
-- 可选：添加复合索引优化查询
-- ==============================================

-- 优化聊天记录查询
CREATE INDEX `idx_chat_history` ON `private_message` (`from_user_id`, `to_user_id`, `create_time`);

-- 优化未读消息查询
CREATE INDEX `idx_unread` ON `private_message` (`to_user_id`, `status`);