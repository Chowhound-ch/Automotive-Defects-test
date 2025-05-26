package per.chowhound.hfut.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
public class UserVo {
    private Long userId;
    private String userName;
    private String avatar;
}
