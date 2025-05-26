package per.chowhound.hfut.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import per.chowhound.hfut.domain.Record;

/**
 * 识别记录Mapper接口
 * 
 * @author chowhound
 * @date 2025-03-19
 */
public interface RecordMapper extends BaseMapper<Record> {
    /**
     * 查询识别记录
     * 
     * @param recordId 识别记录主键
     * @return 识别记录
     */
    public Record selectRecordByRecordId(Long recordId);

    /**
     * 查询识别记录列表
     * 
     * @param record 识别记录
     * @return 识别记录集合
     */
    public List<Record> selectRecordList(Record record);

    /**
     * 新增识别记录
     * 
     * @param record 识别记录
     * @return 结果
     */
    public int insertRecord(Record record);

    /**
     * 修改识别记录
     * 
     * @param record 识别记录
     * @return 结果
     */
    public int updateRecord(Record record);

    /**
     * 删除识别记录
     * 
     * @param recordId 识别记录主键
     * @return 结果
     */
    public int deleteRecordByRecordId(Long recordId);

    /**
     * 批量删除识别记录
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRecordByRecordIds(Long[] recordIds);
}
