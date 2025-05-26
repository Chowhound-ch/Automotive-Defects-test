package per.chowhound.hfut.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import per.chowhound.hfut.domain.Role;
import per.chowhound.hfut.service.IRoleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 角色Controller
 * 
 * @author chowhound
 * @date 2025-03-19
 */
@RestController
@RequestMapping("/role/role")
public class RoleController extends BaseController
{
    @Autowired
    private IRoleService roleService;

    /**
     * 查询角色列表
     */
    @PreAuthorize("@ss.hasPermi('role:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(Role role)
    {
        startPage();
        List<Role> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    /**
     * 导出角色列表
     */
    @PreAuthorize("@ss.hasPermi('role:role:export')")
    @Log(title = "角色", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Role role)
    {
        List<Role> list = roleService.selectRoleList(role);
        ExcelUtil<Role> util = new ExcelUtil<Role>(Role.class);
        util.exportExcel(response, list, "角色数据");
    }

    /**
     * 获取角色详细信息
     */
    @PreAuthorize("@ss.hasPermi('role:role:query')")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable("roleId") Long roleId)
    {
        return success(roleService.selectRoleByRoleId(roleId));
    }

    /**
     * 新增角色
     */
    @PreAuthorize("@ss.hasPermi('role:role:add')")
    @Log(title = "角色", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Role role)
    {
        return toAjax(roleService.insertRole(role));
    }

    /**
     * 修改角色
     */
    @PreAuthorize("@ss.hasPermi('role:role:edit')")
    @Log(title = "角色", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Role role)
    {
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 删除角色
     */
    @PreAuthorize("@ss.hasPermi('role:role:remove')")
    @Log(title = "角色", businessType = BusinessType.DELETE)
	@DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds)
    {
        return toAjax(roleService.deleteRoleByRoleIds(roleIds));
    }
}
