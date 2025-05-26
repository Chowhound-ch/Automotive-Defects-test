package per.chowhound.hfut.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.chowhound.hfut.mapper.DefectMapper;
import per.chowhound.hfut.domain.Defect;
import per.chowhound.hfut.service.IDefectService;

/**
 * 缺陷识别Service业务层处理
 * 
 * @author chowhound
 * @date 2025-03-19
 */
@Service
public class DefectServiceImpl implements IDefectService 
{
    @Autowired
    private DefectMapper defectMapper;

    /**
     * 查询缺陷识别
     * 
     * @param defectId 缺陷识别主键
     * @return 缺陷识别
     */
    @Override
    public Defect selectDefectByDefectId(Long defectId)
    {
        return defectMapper.selectDefectByDefectId(defectId);
    }

    /**
     * 查询缺陷识别列表
     * 
     * @param defect 缺陷识别
     * @return 缺陷识别
     */
    @Override
    public List<Defect> selectDefectList(Defect defect)
    {
        return defectMapper.selectDefectList(defect);
    }

    /**
     * 新增缺陷识别
     * 
     * @param defect 缺陷识别
     * @return 结果
     */
    @Override
    public int insertDefect(Defect defect)
    {
        return defectMapper.insertDefect(defect);
    }

    /**
     * 修改缺陷识别
     * 
     * @param defect 缺陷识别
     * @return 结果
     */
    @Override
    public int updateDefect(Defect defect)
    {
        return defectMapper.updateDefect(defect);
    }

    /**
     * 批量删除缺陷识别
     * 
     * @param defectIds 需要删除的缺陷识别主键
     * @return 结果
     */
    @Override
    public int deleteDefectByDefectIds(Long[] defectIds)
    {
        return defectMapper.deleteDefectByDefectIds(defectIds);
    }

    /**
     * 删除缺陷识别信息
     * 
     * @param defectId 缺陷识别主键
     * @return 结果
     */
    @Override
    public int deleteDefectByDefectId(Long defectId)
    {
        return defectMapper.deleteDefectByDefectId(defectId);
    }
}
