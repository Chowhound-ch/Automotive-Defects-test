package per.chowhound.hfut.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import per.chowhound.hfut.domain.File;
import per.chowhound.hfut.domain.Record;
import per.chowhound.hfut.domain.vo.RecordVo;

/**
 * 识别记录Service接口
 * 
 * @author chowhound
 * @date 2025-03-19
 */
public interface IRecordService extends IService<Record> {

    RecordVo recognize(List<File> files, Record record);

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
     * 批量删除识别记录
     * 
     * @param recordIds 需要删除的识别记录主键集合
     * @return 结果
     */
    public int deleteRecordByRecordIds(Long[] recordIds);

    /**
     * 删除识别记录信息
     * 
     * @param recordId 识别记录主键
     * @return 结果
     */
    public int deleteRecordByRecordId(Long recordId);

    List<RecordVo> listRecord(Record record);

    RecordVo getRecordById(Long recordId);

    List<RecordVo> listRecord(Long userId, Record record);
}
