package per.chowhound.hfut.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.chowhound.hfut.domain.File;
import per.chowhound.hfut.domain.Model;
import per.chowhound.hfut.domain.Result;
import per.chowhound.hfut.domain.vo.RecordVo;
import per.chowhound.hfut.domain.vo.UserVo;
import per.chowhound.hfut.mapper.RecordMapper;
import per.chowhound.hfut.domain.Record;
import per.chowhound.hfut.service.IFileService;
import per.chowhound.hfut.service.IModelService;
import per.chowhound.hfut.service.IRecordService;
import per.chowhound.hfut.service.IResultService;
import per.chowhound.hfut.utils.DefectUtils;

import javax.annotation.Resource;

/**
 * 识别记录Service业务层处理
 * 
 * @author chowhound
 * @date 2025-03-19
 */
@Slf4j
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private IResultService resultService;
    @Resource
    private ISysUserService userService;
    @Autowired
    private IFileService fileService;
    @Autowired
    private IModelService modelService;
    private static Map<String, Long> DEFECT_MAP = Map.of(
            "tire flat",    0b1L,
            "scratch",      0b10L,
            "crack",        0b100L,
            "glass shatter",0b1000L,
            "lamp broken",  0b10000L,
            "dent",         0b100000L
    );

    @Override
    public RecordVo recognize(List<File> files, Record record) {
        Model model = modelService.getById(record.getModel());
        if (files.isEmpty()) return null;
        else if (files.size() == 1) {
            record.setType(0L); //TODO 单张图片
        } else {
            record.setType(1L); //TODO 多张图片
        }
        List<Long> fileIds = files.stream().map(File::getFileId).toList();
        Map<Long, File> fileMap = fileService.listByIds(fileIds).stream().collect(Collectors.toMap(File::getFileId, file -> file));
        StringBuilder builder = new StringBuilder();
        List<Result> results = new ArrayList<>();
        for (File file : files) {
            Result result = recognizeSingle(fileMap.get(file.getFileId()), record.getUserId(), model);
            if (result != null) {
                results.add(result);
                builder.append(result.getResultId()).append(",");
            }
        }
        record.setResult(builder.toString());
        if (StrUtil.isNotBlank(record.getNote())) {
            record.setNote(record.getNote());
        } else {
            record.setNote("识别记录" + DateUtils.getTime());
        }
        save(record);
        RecordVo recordVo = new RecordVo();
        BeanUtils.copyProperties(record, recordVo);
        recordVo.setResults(results);
        return recordVo;
    }

    private Result recognizeSingle(File file, Long userId, Model model) {
        JsonNode node = DefectUtils.doRecognize(file, model.getFileUrl());
        if (node == null) {
            return null;
        }
        Result result = new Result();
        result.setUserId(userId);
        result.setUrl(node.get("data").get("url").asText());
        result.setFileId(file.getFileId());
        result.setDefectId(0L);
        for (JsonNode res : node.get("data").get("res")) {
            if (res.has("class")) {
                Long aClass = DEFECT_MAP.get(res.get("class").asText());
                if (aClass == null) {
                    log.error("缺少class：{}", res.get("class").asText(), new RuntimeException(""));
                    break;
                }
                result.setDefectId(result.getDefectId() | aClass);
            }
        }
        resultService.save(result);
        return result;
    }

    /**
     * 查询识别记录
     * 
     * @param recordId 识别记录主键
     * @return 识别记录
     */
    @Override
    public Record selectRecordByRecordId(Long recordId)
    {
        return recordMapper.selectRecordByRecordId(recordId);
    }

    /**
     * 查询识别记录列表
     * 
     * @param record 识别记录
     * @return 识别记录
     */
    @Override
    public List<Record> selectRecordList(Record record)
    {
        return recordMapper.selectRecordList(record);
    }

    /**
     * 新增识别记录
     * 
     * @param record 识别记录
     * @return 结果
     */
    @Override
    public int insertRecord(Record record)
    {
        record.setCreateTime(DateUtils.getNowDate());
        return recordMapper.insertRecord(record);
    }

    /**
     * 修改识别记录
     * 
     * @param record 识别记录
     * @return 结果
     */
    @Override
    public int updateRecord(Record record)
    {
        record.setUpdateTime(DateUtils.getNowDate());
        return recordMapper.updateRecord(record);
    }

    /**
     * 批量删除识别记录
     * 
     * @param recordIds 需要删除的识别记录主键
     * @return 结果
     */
    @Override
    public int deleteRecordByRecordIds(Long[] recordIds)
    {
        return recordMapper.deleteRecordByRecordIds(recordIds);
    }

    /**
     * 删除识别记录信息
     * 
     * @param recordId 识别记录主键
     * @return 结果
     */
    @Override
    public int deleteRecordByRecordId(Long recordId)
    {
        return recordMapper.deleteRecordByRecordId(recordId);
    }

    @Override
    public List<RecordVo> listRecord(Record record) {
        return listRecord(null, record);
    }

    @Override
    public RecordVo getRecordById(Long recordId) {
        Record record = this.getById(recordId);
        RecordVo recordVo = new RecordVo();
        BeanUtils.copyProperties(record, recordVo);
        Set<Long> res = Arrays.stream(record.getResult().split(","))
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        recordVo.setResults(resultService.listByIds(res));

        return recordVo;
    }

    @Override
    public List<RecordVo> listRecord(Long userId, Record record) {

        LambdaQueryWrapper<Record> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Record::getUserId, record.getUserId());
        wrapper.eq(record.getModel() != null, Record::getModel, record.getModel());
        wrapper.eq(record.getType() != null, Record::getType, record.getType());
        wrapper.eq(userId != null, Record::getUserId, userId);

        List<Record> list = list(wrapper);
        Set<Long> userIds = new HashSet<>();
        Set<Long> resultIds = list.stream().map(recordItem ->{
                    userIds.add(recordItem.getUserId());
                    return recordItem.getResult();
                })
                .flatMap(s ->
                        Stream.of(s.split(","))
                )
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toSet());
        if (resultIds.isEmpty()|| userIds.isEmpty()) {
            return new ArrayList<>();
        }
        Map<Long, Result> resultIdsMap = resultService.listByIds(resultIds)
                .stream()
                .collect(Collectors.toMap(Result::getResultId, result -> result));
        Map<Long, SysUser> userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(SysUser::getUserId, user -> user));
        List<RecordVo> res = new ArrayList<>();
        for (Record rec : list) {
            RecordVo recordVo = new RecordVo();
            BeanUtils.copyProperties(rec, recordVo);
            List<Result> results = Stream.of(rec.getResult().split(","))
                    .filter(s -> !s.isEmpty())
                    .map(Long::parseLong)
                    .map(resultIdsMap::get)
                    .collect(Collectors.toList());
            recordVo.setResults(results);

            SysUser sysUser = userMap.get(rec.getUserId());
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(sysUser, userVo);
            recordVo.setUser(userVo);
            res.add(recordVo);
        }

        return res;
    }
}
