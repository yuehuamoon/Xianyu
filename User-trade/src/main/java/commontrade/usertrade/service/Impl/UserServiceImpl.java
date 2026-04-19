package commontrade.usertrade.service.Impl;

import commontrade.commonyh.pojo.dto.LoginDTO;
import commontrade.commonyh.pojo.entity.User;
import commontrade.commonyh.pojo.vo.UserVO;
import commontrade.usertrade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import commontrade.usertrade.mapper.UserMapper;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    @Override
    public UserVO selectById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> selectByPage(Integer page) {
        return userMapper.selectByPage(page);
    }
    @Override
    public UserVO selectByPhone(String phone){
        return userMapper.selectByPhone(phone);
    }

    @Override
    public UserVO login(LoginDTO user){
        return userMapper.login(user);
    }
    @Override
    public int registerUser(UserVO user){
        List<User> users = userMapper.selectAll();
        for (User user1 : users){
            if (user1.getPhone().equals(user.getPhone())){
                return 0;
            }
        }
        if (user.getRole() == null) {
            user.setRole("1"); // 1=普通用户（适配数据库数字类型）
        }
        return userMapper.registerUser(user);
    }

    @Override
    public int deleteByPhone(UserVO user){
        return userMapper.deleteByPhone(user.getPhone());
    }
    @Override
    public int updateUserSelective(User user){
        return userMapper.updateUserSelective(user);
    }
    @Override
    public int identifyByAdmin(int starus, String phone){
        return userMapper.identifyByAdmin(starus,phone);
    }

    // 统计方法实现
    @Override
    public int countAll() {
        return userMapper.countAll();
    }

    @Override
    public int countByStatus(int status) {
        return userMapper.countByStatus(status);
    }

    @Override
    public int countByRole(int role) {
        return userMapper.countByRole(role);
    }
}
