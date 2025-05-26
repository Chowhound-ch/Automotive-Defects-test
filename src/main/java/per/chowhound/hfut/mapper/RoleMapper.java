package per.chowhound.hfut.mapper;

import java.util.List;
import per.chowhound.hfut.domain.Role;

/**
 * 角色Mapper接口
 * 
 * @author chowhound
 * @date 2025-03-19
 */
public interface RoleMapper 
{
    /**
     * 查询角色
     * 
     * @param roleId 角色主键
     * @return 角色
     */
    public Role selectRoleByRoleId(Long roleId);

    /**
     * 查询角色列表
     * 
     * @param role 角色
     * @return 角色集合
     */
    public List<Role> selectRoleList(Role role);

    /**
     * 新增角色
     * 
     * @param role 角色
     * @return 结果
     */
    public int insertRole(Role role);

    /**
     * 修改角色
     * 
     * @param role 角色
     * @return 结果
     */
    public int updateRole(Role role);

    /**
     * 删除角色
     * 
     * @param roleId 角色主键
     * @return 结果
     */
    public int deleteRoleByRoleId(Long roleId);

    /**
     * 批量删除角色
     * 
     * @param roleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRoleByRoleIds(Long[] roleIds);
}
