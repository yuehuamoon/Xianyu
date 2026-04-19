package commontrade.usertrade.service;

import commontrade.commonyh.pojo.dto.LoginDTO;
import commontrade.commonyh.pojo.entity.User;
import commontrade.commonyh.pojo.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> selectAll();
    UserVO selectById(int id);
    List<User> selectByPage(Integer page);
    UserVO selectByPhone(String phone);
    UserVO login(LoginDTO user);
    int registerUser(UserVO user);
    int deleteByPhone(UserVO user);
    int updateUserSelective(User user);
    int identifyByAdmin(int starus, String phone);

    // 统计方法
    int countAll();
    int countByStatus(int status);
    int countByRole(int role);
}
