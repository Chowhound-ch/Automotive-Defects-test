package per.chowhound.hfut.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.web.service.TokenService;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
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
import per.chowhound.hfut.domain.Defect;
import per.chowhound.hfut.domain.Record;
import per.chowhound.hfut.domain.Result;
import per.chowhound.hfut.domain.vo.RecognizeVo;
import per.chowhound.hfut.domain.vo.RecordVo;
import per.chowhound.hfut.service.IDefectService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import per.chowhound.hfut.service.IRecordService;
import per.chowhound.hfut.service.IResultService;
import per.chowhound.hfut.utils.HttpUtil;

/**
 * 缺陷识别Controller
 * 
 * @author chowhound
 * @date 2025-03-19
 */
@RestController
@RequestMapping("/defect/defect")
public class DefectController extends BaseController
{
    @Autowired
    private IDefectService defectService;
    @Resource
    private TokenService tokenService;
    @Autowired
    private IRecordService recordService;
    @Autowired
    private IResultService resultService;
//    private Map<String, Long> defectMap = Map.of("tire flat", 1L, "scratch", 2L);

    /**
     * 识别接口
     */
    @PostMapping("/recognize")
    public AjaxResult detect(@RequestBody RecognizeVo recognize) {
        LoginUser user = tokenService.getLoginUser(ServletUtils.getRequest());
        Record record = new Record();
        record.setUserId(user.getUserId());
        record.setModel(recognize.getModel());
        record.setNote(recognize.getNote());
        RecordVo res = recordService.recognize(recognize.getFiles(), record);
        if (res == null) {
            return error("识别失败");
        }

        return success(res);
    }

    /**
     * 查询缺陷识别列表
     */
    @PreAuthorize("@ss.hasPermi('defect:defect:list')")
    @GetMapping("/list")
    public TableDataInfo list(Defect defect)
    {
        startPage();
        List<Defect> list = defectService.selectDefectList(defect);
        return getDataTable(list);
    }

    /**
     * 导出缺陷识别列表
     */
    @PreAuthorize("@ss.hasPermi('defect:defect:export')")
    @Log(title = "缺陷识别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Defect defect)
    {
        List<Defect> list = defectService.selectDefectList(defect);
        ExcelUtil<Defect> util = new ExcelUtil<Defect>(Defect.class);
        util.exportExcel(response, list, "缺陷识别数据");
    }

    /**
     * 获取缺陷识别详细信息
     */
    @PreAuthorize("@ss.hasPermi('defect:defect:query')")
    @GetMapping(value = "/{defectId}")
    public AjaxResult getInfo(@PathVariable("defectId") Long defectId)
    {
        return success(defectService.selectDefectByDefectId(defectId));
    }

    /**
     * 新增缺陷识别
     */
    @PreAuthorize("@ss.hasPermi('defect:defect:add')")
    @Log(title = "缺陷识别", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Defect defect)
    {
        return toAjax(defectService.insertDefect(defect));
    }

    /**
     * 修改缺陷识别
     */
    @PreAuthorize("@ss.hasPermi('defect:defect:edit')")
    @Log(title = "缺陷识别", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Defect defect)
    {
        return toAjax(defectService.updateDefect(defect));
    }

    /**
     * 删除缺陷识别
     */
    @PreAuthorize("@ss.hasPermi('defect:defect:remove')")
    @Log(title = "缺陷识别", businessType = BusinessType.DELETE)
	@DeleteMapping("/{defectIds}")
    public AjaxResult remove(@PathVariable Long[] defectIds)
    {
        return toAjax(defectService.deleteDefectByDefectIds(defectIds));
    }
}
