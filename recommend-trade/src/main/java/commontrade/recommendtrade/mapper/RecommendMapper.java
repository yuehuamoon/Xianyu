package commontrade.recommendtrade.mapper;


import commontrade.commonyh.pojo.entity.*;
import commontrade.commonyh.pojo.vo.*;
import commontrade.commonyh.pojo.dto.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecommendMapper {

    @Select("<script>"
            + "SELECT * FROM good_info WHERE id IN "
            + "<foreach collection='ids' item='id' open='(' separator=',' close=')'>"
            +   "#{id}"
        + "</foreach>"
                + "</script>")
    List<Good> selectGoodsByIds(@Param("ids") List<Long> ids);

}
