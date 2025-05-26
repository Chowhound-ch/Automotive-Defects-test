package per.chowhound.hfut.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import per.chowhound.hfut.domain.Model;

/**
 * 模型管理Service接口
 * 
 * @author chowhound
 * @date 2025-03-19
 */
public interface IModelService extends IService<Model> {
    /**
     * 查询模型管理
     * 
     * @param modelId 模型管理主键
     * @return 模型管理
     */
    public Model selectModelByModelId(Long modelId);
    public List<Model> list(Model model);

    /**
     * 查询模型管理列表
     * 
     * @param model 模型管理
     * @return 模型管理集合
     */
    public List<Model> selectModelList(Model model);

    /**
     * 新增模型管理
     * 
     * @param model 模型管理
     * @return 结果
     */
    public int insertModel(Model model);

    /**
     * 修改模型管理
     * 
     * @param model 模型管理
     * @return 结果
     */
    public int updateModel(Model model);

    /**
     * 批量删除模型管理
     * 
     * @param modelIds 需要删除的模型管理主键集合
     * @return 结果
     */
    public int deleteModelByModelIds(Long[] modelIds);

    /**
     * 删除模型管理信息
     * 
     * @param modelId 模型管理主键
     * @return 结果
     */
    public int deleteModelByModelId(Long modelId);

    void update(Model model);
}
