package per.chowhound.hfut.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.chowhound.hfut.mapper.RoleMapper;
import per.chowhound.hfut.domain.Role;
import per.chowhound.hfut.service.IRoleService;

/**
 * 角色Service业务层处理
 * 
 * @author chowhound
 * @date 2025-03-19
 */
@Service
public class RoleServiceImpl implements IRoleService 
{
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 查询角色
     * 
     * @param roleId 角色主键
     * @return 角色
     */
    @Override
    public Role selectRoleByRoleId(Long roleId)
    {
        return roleMapper.selectRoleByRoleId(roleId);
    }

    /**
     * 查询角色列表
     * 
     * @param role 角色
     * @return 角色
     */
    @Override
    public List<Role> selectRoleList(Role role)
    {
        return roleMapper.selectRoleList(role);
    }

    /**
     * 新增角色
     * 
     * @param role 角色
     * @return 结果
     */
    @Override
    public int insertRole(Role role)
    {
        return roleMapper.insertRole(role);
    }

    /**
     * 修改角色
     * 
     * @param role 角色
     * @return 结果
     */
    @Override
    public int updateRole(Role role)
    {
        return roleMapper.updateRole(role);
    }

    /**
     * 批量删除角色
     * 
     * @param roleIds 需要删除的角色主键
     * @return 结果
     */
    @Override
    public int deleteRoleByRoleIds(Long[] roleIds)
    {
        return roleMapper.deleteRoleByRoleIds(roleIds);
    }

    /**
     * 删除角色信息
     * 
     * @param roleId 角色主键
     * @return 结果
     */
    @Override
    public int deleteRoleByRoleId(Long roleId)
    {
        return roleMapper.deleteRoleByRoleId(roleId);
    }
}
