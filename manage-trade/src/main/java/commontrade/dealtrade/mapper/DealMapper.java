package commontrade.managetrade.mapper;



import commontrade.commonyh.pojo.dto.ManageDTO;
import commontrade.commonyh.pojo.entity.Manage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ManageMapper {

    @Select("select * from manage limit #{page}, 50")
    List<ManageDTO> selectAll(int page);

    @Select("select * from manage where id=#{id}")
    ManageDTO selectByID(ManageDTO manage);

    @Select("select * from manage where buyerId=#{id}")
    List<ManageDTO> selectByBuyID(Integer ID);
    @Select("select * from manage where sellerId=#{id}")
    List<ManageDTO> selectBySellerID(Integer ID);
    @Select("select * from manage where goodId=#{id} limit 1")
    ManageDTO selectByGoodID(Integer ID);



    @Insert("INSERT INTO manage (" +
            "sellerId, buyerId, goodId, unitPrice, count, status, " +
            "createTime, updateTime" +
            ") VALUES (" +
            "#{manage.sellerId}, #{manage.buyerId}, #{manage.goodId}, #{manage.unitPrice}, #{manage.count}, 0, " +
            "NOW(), NOW()" +
            ")")
    int insertManage(@Param("manage") ManageDTO manage);


    @Update("update manage set status=2 where id=#{id}")
    int deleteByID(int id);

    /**
     * 选择性更新交易订单（仅更新非空字段）
     * @param manage 交易订单DTO对象（需包含id，用于WHERE条件）
     * @return 受影响的行数（1表示更新成功，0表示无匹配订单）
     */
    @Update({
            "<script>",
            "UPDATE manage",
            "<set>",
            "   <if test='manage.sellerId != null'>sellerId = #{manage.sellerId},</if>",
            "   <if test='manage.buyerId != null'>buyerId = #{manage.buyerId},</if>",
            "   <if test='manage.goodId != null'>goodId = #{manage.goodId},</if>",
            "   <if test='manage.unitPrice != null'>unitPrice = #{manage.unitPrice},</if>",
            "   <if test='manage.count != null'>count = #{manage.count},</if>",
            "   <if test='manage.status != null'>status = #{manage.status},</if>", // 取消订单/议价核心字段
            "   updateTime = CURRENT_TIMESTAMP", // 自动更新修改时间，无需传参
            "</set>",
            "WHERE id = #{manage.id}", // 订单主键，必传
            "</script>"
    })
    int updateManageSelective(@Param("manage") ManageDTO manage);

}
