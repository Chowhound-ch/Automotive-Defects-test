package per.chowhound.hfut.service;

import java.util.List;
import per.chowhound.hfut.domain.Defect;

/**
 * 缺陷识别Service接口
 * 
 * @author chowhound
 * @date 2025-03-19
 */
public interface IDefectService 
{
    /**
     * 查询缺陷识别
     * 
     * @param defectId 缺陷识别主键
     * @return 缺陷识别
     */
    public Defect selectDefectByDefectId(Long defectId);

    /**
     * 查询缺陷识别列表
     * 
     * @param defect 缺陷识别
     * @return 缺陷识别集合
     */
    public List<Defect> selectDefectList(Defect defect);

    /**
     * 新增缺陷识别
     * 
     * @param defect 缺陷识别
     * @return 结果
     */
    public int insertDefect(Defect defect);

    /**
     * 修改缺陷识别
     * 
     * @param defect 缺陷识别
     * @return 结果
     */
    public int updateDefect(Defect defect);

    /**
     * 批量删除缺陷识别
     * 
     * @param defectIds 需要删除的缺陷识别主键集合
     * @return 结果
     */
    public int deleteDefectByDefectIds(Long[] defectIds);

    /**
     * 删除缺陷识别信息
     * 
     * @param defectId 缺陷识别主键
     * @return 结果
     */
    public int deleteDefectByDefectId(Long defectId);
}
