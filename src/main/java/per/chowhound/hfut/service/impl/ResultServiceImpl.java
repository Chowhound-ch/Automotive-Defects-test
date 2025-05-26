package per.chowhound.hfut.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.chowhound.hfut.mapper.ResultMapper;
import per.chowhound.hfut.domain.Result;
import per.chowhound.hfut.service.IResultService;

/**
 * 识别结果Service业务层处理
 * 
 * @author chowhound
 * @date 2025-03-19
 */
@Service
public class ResultServiceImpl extends ServiceImpl<ResultMapper, Result> implements IResultService {
    @Autowired
    private ResultMapper resultMapper;

    /**
     * 查询识别结果
     * 
     * @param resultId 识别结果主键
     * @return 识别结果
     */
    @Override
    public Result selectResultByResultId(Long resultId)
    {
        return resultMapper.selectResultByResultId(resultId);
    }

    /**
     * 查询识别结果列表
     * 
     * @param result 识别结果
     * @return 识别结果
     */
    @Override
    public List<Result> selectResultList(Result result)
    {
        return resultMapper.selectResultList(result);
    }

    /**
     * 新增识别结果
     * 
     * @param result 识别结果
     * @return 结果
     */
    @Override
    public int insertResult(Result result)
    {
        result.setCreateTime(DateUtils.getNowDate());
        return resultMapper.insertResult(result);
    }

    /**
     * 修改识别结果
     * 
     * @param result 识别结果
     * @return 结果
     */
    @Override
    public int updateResult(Result result)
    {
        result.setUpdateTime(DateUtils.getNowDate());
        return resultMapper.updateResult(result);
    }

    /**
     * 批量删除识别结果
     * 
     * @param resultIds 需要删除的识别结果主键
     * @return 结果
     */
    @Override
    public int deleteResultByResultIds(Long[] resultIds)
    {
        return resultMapper.deleteResultByResultIds(resultIds);
    }

    /**
     * 删除识别结果信息
     * 
     * @param resultId 识别结果主键
     * @return 结果
     */
    @Override
    public int deleteResultByResultId(Long resultId)
    {
        return resultMapper.deleteResultByResultId(resultId);
    }
}
