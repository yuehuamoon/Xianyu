package commontrade.communitytrade.mapper;



import commontrade.commonyh.pojo.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommunityMapper{

    @Select("select * from post ORDER BY createTime DESC")
    List<Post> selectPosts();

    @Select("select * from post where id=#{id}")
    Post selectPostById(Integer id);

    @Insert("\n" +
            "INSERT INTO `post` (title,content,status,reviewStatu,userId,userName,imgUrls,ip,createTime,updateTime) VALUES\n" +
            "(#{title},#{content},1,0,#{userId},#{userName},'[\"food.jpg\"]',NULL,NOW(),NOW());")
    int insertPost(Post post);

    @Update("update post set status=0 where id=#{id}")
    int deletePost(Integer id);

    @Update("<script>" +
            "UPDATE post " +
            "<set>" +
            "   <if test='title != null'>title = #{title},</if>" +
            "   <if test='content != null'>content = #{content},</if>" +
            "   <if test='status != null'>status = #{status},</if>" +
            "   <if test='reviewStatu != null'>reviewStatu = #{reviewStatu},</if>" +
            "   <if test='imgUrls != null'>imgUrls = #{imgUrls},</if>" +
            "   <if test='likes != null'>likes = #{likes},</if>" +
            "   <if test='favorite != null'>favorite = #{favorite},</if>" +
            "   <if test='ip != null'>ip = #{ip},</if>" +
            "   updateTime = NOW()" +
            "</set>" +
            "WHERE id = #{id}" +
            "</script>")
    int updatePost(Post post);

    @Select("select  * from comment_good where deleteStatu=0 and status=1 and goodId=#{id}")
    List<CommentGood> selectCommentGood(Integer id);

    @Select("select * from comment_user where deleteStatu=0 and status=1 and toUserId=#{id}")
    List<CommentUser> selectCommentUser(Integer id);

    @Select("select * from comment_post where deleteStatu=0 and status=1 and postId=#{id}")
    List<CommentPost> selectCommentPost(Integer id);


    @Insert("""
        <script>
        INSERT INTO comment_good
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="id != null">id,</if>
            <if test="userId != null">userId,</if>
            <if test="userName != null and userName != ''">userName,</if>
            <if test="goodId != null">goodId,</if>
            <if test="goodName != null and goodName != ''">goodName,</if>
            <if test="attitude != null">attitude,</if>
            <if test="content != null and content != ''">content,</if>
            <if test="deleteStatu != null">deleteStatu,</if>
            <if test="status != null">status,</if>
            <if test="reviewStatu != null">reviewStatu,</if>
            <if test="toUser != null">toUser,</if>
            <if test="likes != null">likes,</if>
            <if test="createTime != null">createTime,</if>
            <if test="updateTime != null">updateTime,</if>
            <if test="deleteTime != null">deleteTime,</if>
            <if test="ip != null and ip != ''">ip,</if>
        </trim>
        <trim prefix="VALUES (" suffix=")" suffixOverrides=",">
            <if test="id != null">#{id},</if>
            <if test="userId != null">#{userId},</if>
            <if test="userName != null and userName != ''">#{userName},</if>
            <if test="goodId != null">#{goodId},</if>
            <if test="goodName != null and goodName != ''">#{goodName},</if>
            <if test="attitude != null">#{attitude},</if>
            <if test="content != null and content != ''">#{content},</if>
            <if test="deleteStatu != null">#{deleteStatu},</if>
            <if test="status != null">#{status},</if>
            <if test="reviewStatu != null">#{reviewStatu},</if>
            <if test="toUser != null">#{toUser},</if>
            <if test="likes != null">#{likes},</if>
            <if test="createTime != null">#{createTime},</if>
            <if test="updateTime != null">#{updateTime},</if>
            <if test="deleteTime != null">#{deleteTime},</if>
            <if test="ip != null and ip != ''">#{ip},</if>
        </trim>
        </script>
    """)
    int insertCommentGood(CommentGood commentGood);


    @Insert("""
        <script>
        INSERT INTO comment_user
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="id != null">id,</if>
            <if test="userId != null">userId,</if>
            <if test="userName != null and userName != ''">userName,</if>
            <if test="toUserId != null">toUserId,</if>
            <if test="toUserName != null and toUserName != ''">toUserName,</if>
            <if test="attitude != null">attitude,</if>
            <if test="content != null and content != ''">content,</if>
            <if test="deleteStatu != null">deleteStatu,</if>
            <if test="status != null">status,</if>
            <if test="reviewStatu != null">reviewStatu,</if>
            <if test="likes != null">likes,</if>
            <if test="createTime != null">createTime,</if>
            <if test="updateTime != null">updateTime,</if>
            <if test="deleteTime != null">deleteTime,</if>
            <if test="ip != null and ip != ''">ip,</if>
        </trim>
        <trim prefix="VALUES (" suffix=")" suffixOverrides=",">
            <if test="id != null">#{id},</if>
            <if test="userId != null">#{userId},</if>
            <if test="userName != null and userName != ''">#{userName},</if>
            <if test="toUserId != null">#{toUserId},</if>
            <if test="toUserName != null and toUserName != ''">#{toUserName},</if>
            <if test="attitude != null">#{attitude},</if>
            <if test="content != null and content != ''">#{content},</if>
            <if test="deleteStatu != null">#{deleteStatu},</if>
            <if test="status != null">#{status},</if>
            <if test="reviewStatu != null">#{reviewStatu},</if>
            <if test="likes != null">#{likes},</if>
            <if test="createTime != null">#{createTime},</if>
            <if test="updateTime != null">#{updateTime},</if>
            <if test="deleteTime != null">#{deleteTime},</if>
            <if test="ip != null and ip != ''">#{ip},</if>
        </trim>
        </script>
    """)
    int insertCommentUser(CommentUser commentUser);



    @Update("UPDATE ${table} SET deleteStatu = #{status} WHERE id = #{id}")
    int updateCommentDeleteStatus(
            @Param("table") String table,
            @Param("id") Integer id,
            @Param("status") Integer status
    );

    // ====================== 评论举报（统一通用方法） ======================
    @Update("UPDATE ${table} SET reviewStatu = 1 WHERE id = #{id}")
    int reportComment(
            @Param("table") String table,
            @Param("id") Integer id
    );

    @Insert("INSERT INTO favorite (userId, targetId, type, status, updateTime, ip) " +
            "VALUES (#{userId}, #{targetId}, #{type}, 1, NOW(), #{ip}) " +
            "ON DUPLICATE KEY UPDATE " +
            "status = IF(status = 1, 0, 1), " +
            "updateTime = NOW()")
    int toggleFavorite(Favorite favorite);

    @Insert("INSERT INTO likes (userId, targetId, type, status, updateTime, ip) " +
            "VALUES (#{userId}, #{targetId}, #{type}, 1, NOW(), #{ip}) " +
            "ON DUPLICATE KEY UPDATE " +
            "status = IF(status = 1, 0, 1), " +
            "updateTime = NOW()")
    int toggleLike(Likes likes);


    @Insert("<script>" +
            "INSERT INTO comment_post " +
            "<trim prefix='(' suffix=')' suffixOverrides=','>" +
            "   <if test='userId != null'>userId,</if>" +
            "   <if test='userName != null'>userName,</if>" +
            "   <if test='postId != null'>postId,</if>" +
            "   <if test='index != null'>`index`,</if>" +
            "   <if test='content != null'>content,</if>" +
            "   <if test='ip != null'>ip,</if>" +
            "</trim>" +
            "<trim prefix='VALUES (' suffix=')' suffixOverrides=','>" +
            "   <if test='userId != null'>#{userId},</if>" +
            "   <if test='userName != null'>#{userName},</if>" +
            "   <if test='postId != null'>#{postId},</if>" +
            "   <if test='index != null'>#{index},</if>" +
            "   <if test='content != null'>#{content},</if>" +
            "   <if test='ip != null'>#{ip},</if>" +
            "</trim>" +
            "</script>")
    int insertCommentPost(CommentPost commentPost);
}
