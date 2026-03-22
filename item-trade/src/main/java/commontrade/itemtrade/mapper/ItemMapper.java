package commontrade.itemtrade.mapper;


import commontrade.commonyh.pojo.entity.Good;
import commontrade.commonyh.pojo.dto.GoodDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ItemMapper {

    @Select("select * from good_info limit #{page}, 10")
    List<Good> selectAll(int page);

    @Select("select * from good_info where label like concat('%', #{type}, '%')")
    List<GoodDTO> selectByLabel(String type);

    @Select("select * from good_info where id=#{id}")
    GoodDTO selectById(int id);

    @Select("select * from good_info where name like concat('%', #{keyword}, '%') " +
            "or content like concat('%', #{keyword}, '%')" +
            "or label like concat('%', #{keyword}, '%')" +
            "or address like concat('%', #{keyword}, '%')")
    List<GoodDTO> search(String keyword);


    @Insert("INSERT INTO good_info (" +
            "name, content, unit_price, count, img_url, label, " +
            "status, address, view_count, favorite_count" +
            ") VALUES (" +
            "#{good.name}, #{good.content}, #{good.unitPrice}, #{good.count}, #{good.imgUrl}, #{good.label}, " +
            "#{good.status}, #{good.address}, #{good.viewCount}, #{good.favoriteCount}" +
            ")")
    int insertGood(@Param("good") GoodDTO good);

    @Delete("delete from good_info where id=#{id}")
    int deleteByID(int id);

    @Update({
            "<script>",
            "UPDATE good_info",
            "<set>",
            "   <if test='good.sellerId != null'>seller_id = #{good.sellerId},</if>",
            "   <if test='good.name != null and good.name != \"\"'>name = #{good.name},</if>",
            "   <if test='good.content != null and good.content != \"\"'>content = #{good.content},</if>",
            "   <if test='good.unitPrice != null'>unit_price = #{good.unitPrice},</if>",
            "   <if test='good.count != null'>count = #{good.count},</if>",
            "   <if test='good.imgUrl != null and good.imgUrl != \"\"'>img_url = #{good.imgUrl},</if>",
            "   <if test='good.label != null and good.label != \"\"'>label = #{good.label},</if>",
            "   <if test='good.status != null'>status = #{good.status},</if>",
            "   <if test='good.address != null and good.address != \"\"'>address = #{good.address},</if>",
            "   <if test='good.viewCount != null'>view_count = #{good.viewCount},</if>",
            "   <if test='good.favoriteCount != null'>favorite_count = #{good.favoriteCount},</if>",
            "   update_time = CURRENT_TIMESTAMP",
            "</set>",
            "WHERE id = #{good.id}",
            "</script>"
    })
    int updateItemSelective(@Param("good") GoodDTO good);


    @Update("update good_info set status=#{status} where id=#{id}")
    int updateGood(int status, int id);



}
