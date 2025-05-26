package per.chowhound.hfut.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.crypto.Mode;
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
import per.chowhound.hfut.domain.Model;
import per.chowhound.hfut.service.IModelService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import per.chowhound.hfut.utils.HttpUtil;

/**
 * 模型管理Controller
 * 
 * @author chowhound
 * @date 2025-03-19
 */
@RestController
@RequestMapping("/model/model")
public class ModelController extends BaseController
{
    @Autowired
    private IModelService modelService;


    /**
     * 新增模型管理
     */
    @PreAuthorize("@ss.hasPermi('model:model:add')")
    @Log(title = "模型管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Model model) {

        return toAjax(modelService.save(model));
    }


    /**
     * 查询模型管理列表
     */
    @PreAuthorize("@ss.hasPermi('model:model:list')")
    @GetMapping("/list")
    public TableDataInfo list(Model model)
    {
        List<Model> list = modelService.list(model);
        return getDataTable(list);
    }

    /**
     * 导出模型管理列表
     */
    @PreAuthorize("@ss.hasPermi('model:model:export')")
    @Log(title = "模型管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Model model)
    {
        List<Model> list = modelService.selectModelList(model);
        ExcelUtil<Model> util = new ExcelUtil<Model>(Model.class);
        util.exportExcel(response, list, "模型管理数据");
    }

    /**
     * 获取模型管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('model:model:query')")
    @GetMapping(value = "/{modelId}")
    public AjaxResult getInfo(@PathVariable("modelId") Long modelId)
    {
        return success(modelService.getById(modelId));
    }


    /**
     * 修改模型管理
     */
    @PreAuthorize("@ss.hasPermi('model:model:edit')")
    @Log(title = "模型管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Model model)
    {
        modelService.update(model);
        return success();
    }

    /**
     * 删除模型管理
     */
    @PreAuthorize("@ss.hasPermi('model:model:remove')")
    @Log(title = "模型管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{modelIds}")
    public AjaxResult remove(@PathVariable Long[] modelIds)
    {
        return toAjax(modelService.deleteModelByModelIds(modelIds));
    }
}
