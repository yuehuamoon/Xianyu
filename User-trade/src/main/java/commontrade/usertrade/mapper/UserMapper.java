package commontrade.usertrade.mapper;


import commontrade.commonyh.pojo.entity.User;
import commontrade.commonyh.pojo.vo.UserVO;
import commontrade.commonyh.pojo.dto.LoginDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user")
    List<User> selectAll();

    @Select("select * from user limit #{page}, 10")
    List<User> selectByPage(Integer page);
    @Select("select * from user where phone=#{phone}")
    UserVO selectByPhone(String phone);

    @Select("select  * from user where id=#{id}")
    UserVO selectById(int id);

    @Select("select * from user where phone=#{user.phone} and password=#{user.password}")
    UserVO login(@Param("user") LoginDTO user);

    @Select("select * from user where id=#{id}")
    User selectByID(int id);

    @Insert("INSERT INTO user (realName, nickName, password, wallet, role, phone, address, gender, avatarUrl, certNo, status, email) " +
            "VALUES (#{realName}, #{nickName}, #{password}, #{wallet}, #{role}, #{phone}, #{address}, #{gender}, #{avatarUrl}, #{certNo}, 1, #{email})")
    int registerUser(UserVO user);

    @Delete("delete from user where phone=#{phone}")
    int deleteByPhone(String phone);



    @Update({
            "<script>",
            "UPDATE user ", // 替换为你的实际表名
            "<set>",
            "   <if test='user.realName != null and user.realName != \"\"'>realName = #{user.realName},</if>",
            "   <if test='user.nickName != null and user.nickName != \"\"'>nickName = #{user.nickName},</if>",
            "   <if test='user.phone != null and user.phone != \"\"'>phone = #{user.phone},</if>",
            "   <if test='user.role != null'>role = #{user.role},</if>",
            "   <if test='user.status != null'>status = #{user.status},</if>",
            "   <if test='user.wallet != null'>wallet = #{user.wallet},</if>",
            "   <if test='user.address != null and user.address != \"\"'>address = #{user.address},</if>",
            // 删掉这行！！！（UserVO 中无 updateTime 字段）
            // "   <if test='user.updateTime != null'>update_time = #{user.updateTime},</if>",
            "</set>",
            "WHERE id = #{user.id}",
            "</script>"
    })
    int updateUserSelective(@Param("user") User user);

    @Update("update user set status=#{status} where phone=#{phone}")
    int identifyByAdmin(int status, @Param("phone") String phone);

    // 统计接口
    @Select("select count(*) from user")
    int countAll();

    @Select("select count(*) from user where status = #{status}")
    int countByStatus(@Param("status") int status);

    @Select("select count(*) from user where role = #{role}")
    int countByRole(@Param("role") int role);

}
