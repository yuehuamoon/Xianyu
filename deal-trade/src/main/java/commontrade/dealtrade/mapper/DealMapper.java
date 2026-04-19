package commontrade.dealtrade.mapper;



import commontrade.commonyh.pojo.dto.DealDTO;
import commontrade.commonyh.pojo.entity.Deal;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DealMapper {

    @Select("select * from deal limit #{page}, 10")
    List<DealDTO> selectAll(int page);

    @Select("select * from deal where id=#{id}")
    DealDTO selectByID(DealDTO deal);

    @Select("select * from deal where buyerId=#{id}")
    List<DealDTO> selectByBuyID(Integer ID);
    @Select("select * from deal where sellerId=#{id}")
    List<DealDTO> selectBySellerID(Integer ID);
    @Select("select * from deal where goodId=#{id} limit 1")
    DealDTO selectByGoodID(Integer ID);



    @Insert("INSERT INTO deal (" +
            "sellerId, buyerId, goodId, unitPrice, count, status, " +
            "createTime, updateTime" +
            ") VALUES (" +
            "#{deal.sellerId}, #{deal.buyerId}, #{deal.goodId}, #{deal.unitPrice}, #{deal.count}, 0, " +
            "NOW(), NOW()" +
            ")")
    int insertDeal(@Param("deal") DealDTO deal);


    @Update("update deal set status=2 where id=#{id}")
    int deleteByID(int id);

    /**
     * 选择性更新交易订单（仅更新非空字段）
     * @param deal 交易订单DTO对象（需包含id，用于WHERE条件）
     * @return 受影响的行数（1表示更新成功，0表示无匹配订单）
     */
    @Update({
            "<script>",
            "UPDATE deal",
            "<set>",
            "   <if test='deal.sellerId != null'>sellerId = #{deal.sellerId},</if>",
            "   <if test='deal.buyerId != null'>buyerId = #{deal.buyerId},</if>",
            "   <if test='deal.goodId != null'>goodId = #{deal.goodId},</if>",
            "   <if test='deal.unitPrice != null'>unitPrice = #{deal.unitPrice},</if>",
            "   <if test='deal.count != null'>count = #{deal.count},</if>",
            "   <if test='deal.status != null'>status = #{deal.status},</if>", // 取消订单/议价核心字段
            "   updateTime = CURRENT_TIMESTAMP", // 自动更新修改时间，无需传参
            "</set>",
            "WHERE id = #{deal.id}", // 订单主键，必传
            "</script>"
    })
    int updateDealSelective(@Param("deal") DealDTO deal);

    // 统计接口
    @Select("select count(*) from deal")
    int countAll();

    @Select("select count(*) from deal where status = #{status}")
    int countByStatus(@Param("status") int status);

    @Select("select sum(unitPrice * count) from deal where status = 3")
    Long sumCompletedAmount();

}
