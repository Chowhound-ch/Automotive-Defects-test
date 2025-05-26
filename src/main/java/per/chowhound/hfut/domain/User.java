package per.chowhound.hfut.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户对象 user
 * 
 * @author chowhound
 * @date 2025-03-20
 */
public class User extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long userId;

    /** 用户名 */
    @Excel(name = "用户名")
    private String username;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 角色id */
    @Excel(name = "角色id")
    private Long roleId;

    /** 是否被删除 */
    @Excel(name = "是否被删除")
    private Integer isDelete;

    /** 电话 */
    @Excel(name = "电话")
    private String phone;

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }

    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
    }

    public void setRoleId(Long roleId) 
    {
        this.roleId = roleId;
    }

    public Long getRoleId() 
    {
        return roleId;
    }

    public void setIsDelete(Integer isDelete)
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete()
    {
        return isDelete;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("avatar", getAvatar())
            .append("roleId", getRoleId())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .append("isDelete", getIsDelete())
            .append("phone", getPhone())
            .toString();
    }
}
