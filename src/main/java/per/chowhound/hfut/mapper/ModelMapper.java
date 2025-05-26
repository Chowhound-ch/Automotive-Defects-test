package per.chowhound.hfut.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import per.chowhound.hfut.domain.Model;

/**
 * 模型管理Mapper接口
 * 
 * @author chowhound
 * @date 2025-03-19
 */
public interface ModelMapper extends BaseMapper<Model> {
    /**
     * 查询模型管理
     * 
     * @param modelId 模型管理主键
     * @return 模型管理
     */
    public Model selectModelByModelId(Long modelId);

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
     * 删除模型管理
     * 
     * @param modelId 模型管理主键
     * @return 结果
     */
    public int deleteModelByModelId(Long modelId);

    /**
     * 批量删除模型管理
     * 
     * @param modelIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteModelByModelIds(Long[] modelIds);
}
