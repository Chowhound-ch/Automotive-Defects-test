package per.chowhound.hfut.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.chowhound.hfut.mapper.UserMapper;
import per.chowhound.hfut.domain.User;
import per.chowhound.hfut.service.IUserService;

/**
 * 用户Service业务层处理
 * 
 * @author chowhound
 * @date 2025-03-20
 */
@Service
public class UserServiceImpl implements IUserService 
{
    @Autowired
    private UserMapper userMapper;

    /**
     * 查询用户
     * 
     * @param userId 用户主键
     * @return 用户
     */
    @Override
    public User selectUserByUserId(Long userId)
    {
        return userMapper.selectUserByUserId(userId);
    }

    /**
     * 查询用户列表
     * 
     * @param user 用户
     * @return 用户
     */
    @Override
    public List<User> selectUserList(User user)
    {
        return userMapper.selectUserList(user);
    }

    /**
     * 新增用户
     * 
     * @param user 用户
     * @return 结果
     */
    @Override
    public int insertUser(User user)
    {
        user.setCreateTime(DateUtils.getNowDate());
        return userMapper.insertUser(user);
    }

    /**
     * 修改用户
     * 
     * @param user 用户
     * @return 结果
     */
    @Override
    public int updateUser(User user)
    {
        user.setUpdateTime(DateUtils.getNowDate());
        return userMapper.updateUser(user);
    }

    /**
     * 批量删除用户
     * 
     * @param userIds 需要删除的用户主键
     * @return 结果
     */
    @Override
    public int deleteUserByUserIds(Long[] userIds)
    {
        return userMapper.deleteUserByUserIds(userIds);
    }

    /**
     * 删除用户信息
     * 
     * @param userId 用户主键
     * @return 结果
     */
    @Override
    public int deleteUserByUserId(Long userId)
    {
        return userMapper.deleteUserByUserId(userId);
    }
}
