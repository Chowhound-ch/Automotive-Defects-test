package per.chowhound.hfut.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.chowhound.hfut.mapper.ModelMapper;
import per.chowhound.hfut.domain.Model;
import per.chowhound.hfut.service.IModelService;

/**
 * 模型管理Service业务层处理
 * 
 * @author chowhound
 * @date 2025-03-19
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements IModelService
{
    @Autowired
    private ModelMapper modelMapper;

    /**
     * 查询模型管理
     * 
     * @param modelId 模型管理主键
     * @return 模型管理
     */
    @Override
    public Model selectModelByModelId(Long modelId)
    {
        return modelMapper.selectModelByModelId(modelId);
    }

    @Override
    public List<Model> list(Model model) {
        LambdaQueryWrapper<Model> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(model.getUrl() != null, Model::getUrl, model.getUrl())
                .eq(model.getDetail() != null, Model::getDetail, model.getDetail())
                .eq(model.getFileUrl()!= null, Model::getFileUrl, model.getFileUrl())
                .eq(model.getNote() !=null, Model::getNote, model.getNote());

        return modelMapper.selectList(wrapper);
    }

    /**
     * 查询模型管理列表
     * 
     * @param model 模型管理
     * @return 模型管理
     */
    @Override
    public List<Model> selectModelList(Model model)
    {
        return modelMapper.selectModelList(model);
    }

    /**
     * 新增模型管理
     * 
     * @param model 模型管理
     * @return 结果
     */
    @Override
    public int insertModel(Model model)
    {
        return modelMapper.insertModel(model);
    }

    /**
     * 修改模型管理
     * 
     * @param model 模型管理
     * @return 结果
     */
    @Override
    public int updateModel(Model model)
    {
        return modelMapper.updateModel(model);
    }

    /**
     * 批量删除模型管理
     * 
     * @param modelIds 需要删除的模型管理主键
     * @return 结果
     */
    @Override
    public int deleteModelByModelIds(Long[] modelIds)
    {
        return modelMapper.deleteModelByModelIds(modelIds);
    }

    /**
     * 删除模型管理信息
     * 
     * @param modelId 模型管理主键
     * @return 结果
     */
    @Override
    public int deleteModelByModelId(Long modelId)
    {
        return modelMapper.deleteModelByModelId(modelId);
    }

    @Override
    public void update(Model model) {
        LambdaUpdateWrapper<Model> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Model::getModelId, model.getModelId())
                .set(model.getUrl() != null, Model::getUrl, model.getUrl())
                .set(model.getDetail() != null, Model::getDetail, model.getDetail())
                .set(model.getFileUrl()!= null, Model::getFileUrl, model.getFileUrl())
                .set(model.getNote() !=null, Model::getNote, model.getNote());
        modelMapper.update(model, wrapper);
    }
}
