package commontrade.commonyh.mapper;

import commontrade.commonyh.pojo.entity.CommentPost;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentPostMapper {

    @Insert("""
        <script>
        INSERT INTO comment_post
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="id != null">id,</if>
            <if test="userId != null">user_id,</if>
            <if test="userName != null and userName != ''">user_name,</if>
            <if test="postId != null">post_id,</if>
            <if test="index != null">`index`,</if>
            <if test="content != null and content != ''">content,</if>
            <if test="deleteStatu != null">delete_statu,</if>
            <if test="status != null">status,</if>
            <if test="reviewStatu != null">review_statu,</if>
            <if test="likes != null">likes,</if>
            <if test="createTime != null">create_time,</if>
            <if test="updateTime != null">update_time,</if>
            <if test="deleteTime != null">delete_time,</if>
            <if test="ip != null and ip != ''">ip,</if>
        </trim>
        <trim prefix="VALUES (" suffix=")" suffixOverrides=",">
            <if test="id != null">#{id},</if>
            <if test="userId != null">#{userId},</if>
            <if test="userName != null and userName != ''">#{userName},</if>
            <if test="postId != null">#{postId},</if>
            <if test="index != null">#{index},</if>
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
    int insertCommentPost(CommentPost commentPost);
}