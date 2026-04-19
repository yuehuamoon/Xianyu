package commontrade.commonyh.mapper;

import commontrade.commonyh.pojo.entity.Good;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodMapper {

    @Insert("""
        <script>
        INSERT INTO good_info
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="id != null">id,</if>
            <if test="sellerId != null">seller_id,</if>
            <if test="name != null and name != ''">name,</if>
            <if test="content != null and content != ''">content,</if>
            <if test="unitPrice != null">unit_price,</if>
            <if test="count != null">count,</if>
            <if test="imgUrl != null and imgUrl != ''">img_url,</if>
            <if test="label != null and label != ''">label,</if>
            <if test="status != null">status,</if>
            <if test="address != null and address != ''">address,</if>
            <if test="viewCount != null">view_count,</if>
            <if test="favoriteCount != null">favorite_count,</if>
            <if test="createTime != null">create_time,</if>
            <if test="updateTime != null">update_time,</if>
            <if test="deleteTime != null">delete_time,</if>
        </trim>
        <trim prefix="VALUES (" suffix=")" suffixOverrides=",">
            <if test="id != null">#{id},</if>
            <if test="sellerId != null">#{sellerId},</if>
            <if test="name != null and name != ''">#{name},</if>
            <if test="content != null and content != ''">#{content},</if>
            <if test="unitPrice != null">#{unitPrice},</if>
            <if test="count != null">#{count},</if>
            <if test="imgUrl != null and imgUrl != ''">#{imgUrl},</if>
            <if test="label != null and label != ''">#{label},</if>
            <if test="status != null">#{status},</if>
            <if test="address != null and address != ''">#{address},</if>
            <if test="viewCount != null">#{viewCount},</if>
            <if test="favoriteCount != null">#{favoriteCount},</if>
            <if test="createTime != null">#{createTime},</if>
            <if test="updateTime != null">#{updateTime},</if>
            <if test="deleteTime != null">#{deleteTime},</if>
        </trim>
        </script>
    """)
    int insertGood(Good good);
}