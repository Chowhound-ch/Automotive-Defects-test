package per.chowhound.hfut.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.web.service.TokenService;
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
import per.chowhound.hfut.domain.Record;
import per.chowhound.hfut.domain.vo.RecordVo;
import per.chowhound.hfut.service.IRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 识别记录Controller
 * 
 * @author chowhound
 * @date 2025-03-19
 */
@RestController
@RequestMapping("/record/record")
public class RecordController extends BaseController
{
    @Autowired
    private IRecordService recordService;
    @Resource
    private TokenService tokenService;
    public static final String ADMIN_PERMISSION = "*:*:*";

    /**
     * 查询识别记录列表
     */
    @PreAuthorize("@ss.hasPermi('record:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(Record record)
    {
        startPage();
        LoginUser user = tokenService.getLoginUser(ServletUtils.getRequest());
        record.setUserId(user.getUserId());
        List<RecordVo> list;
        if (user.getPermissions().contains(ADMIN_PERMISSION)) {
            list = recordService.listRecord(record);
        }else {
            list = recordService.listRecord(user.getUserId(), record);
        }
        TableDataInfo table = getDataTable(list);
        table.setTotal(recordService.count());
        return table;
    }
    @PreAuthorize("@ss.hasPermi('record:record:list')")
    @GetMapping("/{recordId}")
    public AjaxResult getOneById(@PathVariable Long recordId)
    {
        RecordVo record = recordService.getRecordById(recordId);

        return success(record);
    }
    /**
     * 导出识别记录列表
     */
    @PreAuthorize("@ss.hasPermi('record:record:export')")
    @Log(title = "识别记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Record record)
    {
        List<Record> list = recordService.selectRecordList(record);
        ExcelUtil<Record> util = new ExcelUtil<Record>(Record.class);
        util.exportExcel(response, list, "识别记录数据");
    }

    /**
     * 新增识别记录
     */
    @PreAuthorize("@ss.hasPermi('record:record:add')")
    @Log(title = "识别记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Record record)
    {
        return toAjax(recordService.insertRecord(record));
    }

    /**
     * 修改识别记录
     */
    @PreAuthorize("@ss.hasPermi('record:record:edit')")
    @Log(title = "识别记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Record record)
    {
        return toAjax(recordService.updateRecord(record));
    }

    /**
     * 删除识别记录
     */
    @PreAuthorize("@ss.hasPermi('record:record:remove')")
    @Log(title = "识别记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(recordService.deleteRecordByRecordIds(recordIds));
    }
}
