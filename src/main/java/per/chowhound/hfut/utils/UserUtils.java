package per.chowhound.hfut.utils;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.bean.BeanUtils;
import per.chowhound.hfut.domain.vo.UserVo;

public class UserUtils {

    public static UserVo getUser(SysUser user) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }
}
