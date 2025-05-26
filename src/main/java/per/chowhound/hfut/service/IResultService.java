package per.chowhound.hfut.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import per.chowhound.hfut.domain.Result;

/**
 * 识别结果Service接口
 * 
 * @author chowhound
 * @date 2025-03-19
 */
public interface IResultService extends IService<Result> {
    /**
     * 查询识别结果
     * 
     * @param resultId 识别结果主键
     * @return 识别结果
     */
    public Result selectResultByResultId(Long resultId);

    /**
     * 查询识别结果列表
     * 
     * @param result 识别结果
     * @return 识别结果集合
     */
    public List<Result> selectResultList(Result result);

    /**
     * 新增识别结果
     * 
     * @param result 识别结果
     * @return 结果
     */
    public int insertResult(Result result);

    /**
     * 修改识别结果
     * 
     * @param result 识别结果
     * @return 结果
     */
    public int updateResult(Result result);

    /**
     * 批量删除识别结果
     * 
     * @param resultIds 需要删除的识别结果主键集合
     * @return 结果
     */
    public int deleteResultByResultIds(Long[] resultIds);

    /**
     * 删除识别结果信息
     * 
     * @param resultId 识别结果主键
     * @return 结果
     */
    public int deleteResultByResultId(Long resultId);
}
