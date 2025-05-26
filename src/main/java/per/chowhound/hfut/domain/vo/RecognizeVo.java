package per.chowhound.hfut.domain.vo;

import lombok.Data;
import per.chowhound.hfut.domain.File;

import java.util.List;

@Data
public class RecognizeVo {
    private Long model;
    private List<File> files;
    private String note;
}
