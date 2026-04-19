package commontrade.commonyh.mapper;

import commontrade.commonyh.pojo.entity.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {

    @Insert("""
        <script>
        INSERT INTO post
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="id != null">id,</if>
            <if test="title != null and title != ''">title,</if>
            <if test="content != null and content != ''">content,</if>
            <if test="status != null">status,</if>
            <if test="reviewStatu != null">review_statu,</if>
            <if test="userId != null">user_id,</if>
            <if test="userName != null and userName != ''">user_name,</if>
            <if test="imgUrls != null and imgUrls != ''">img_urls,</if>
            <if test="likes != null">likes,</if>
            <if test="favorite != null">favorite,</if>
            <if test="ip != null and ip != ''">ip,</if>
            <if test="createTime != null">create_time,</if>
            <if test="updateTime != null">update_time,</if>
        </trim>
        <trim prefix="VALUES (" suffix=")" suffixOverrides=",">
            <if test="id != null">#{id},</if>
            <if test="title != null and title != ''">#{title},</if>
            <if test="content != null and content != ''">#{content},</if>
            <if test="status != null">#{status},</if>
            <if test="reviewStatu != null">#{reviewStatu},</if>
            <if test="userId != null">#{userId},</if>
            <if test="userName != null and userName != ''">#{userName},</if>
            <if test="imgUrls != null and imgUrls != ''">#{imgUrls},</if>
            <if test="likes != null">#{likes},</if>
            <if test="favorite != null">#{favorite},</if>
            <if test="ip != null and ip != ''">#{ip},</if>
            <if test="createTime != null">#{createTime},</if>
            <if test="updateTime != null">#{updateTime},</if>
        </trim>
        </script>
    """)
    int insertPost(Post post);
}