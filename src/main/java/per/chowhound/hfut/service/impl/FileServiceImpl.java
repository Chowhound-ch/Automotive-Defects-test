package per.chowhound.hfut.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.chowhound.hfut.domain.Record;
import per.chowhound.hfut.mapper.FileMapper;
import per.chowhound.hfut.domain.File;
import per.chowhound.hfut.service.IFileService;
import per.chowhound.hfut.service.IRecordService;

/**
 * 文件上传Service业务层处理
 * 
 * @author chowhound
 * @date 2025-03-19
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService
{
    @Autowired
    private FileMapper fileMapper;

    /**
     * 查询文件上传
     * 
     * @param fileId 文件上传主键
     * @return 文件上传
     */
    @Override
    public File selectFileByFileId(Long fileId)
    {
        return fileMapper.selectFileByFileId(fileId);
    }

    /**
     * 查询文件上传列表
     * 
     * @param file 文件上传
     * @return 文件上传
     */
    @Override
    public List<File> selectFileList(File file)
    {
        return fileMapper.selectFileList(file);
    }

    /**
     * 新增文件上传
     * 
     * @param file 文件上传
     * @return 结果
     */
    @Override
    public int insertFile(File file)
    {
        file.setCreateTime(DateUtils.getNowDate());
        return fileMapper.insertFile(file);
    }

    /**
     * 修改文件上传
     * 
     * @param file 文件上传
     * @return 结果
     */
    @Override
    public int updateFile(File file)
    {
        file.setUpdateTime(DateUtils.getNowDate());
        return fileMapper.updateFile(file);
    }

    /**
     * 批量删除文件上传
     * 
     * @param fileIds 需要删除的文件上传主键
     * @return 结果
     */
    @Override
    public int deleteFileByFileIds(Long[] fileIds)
    {
        return fileMapper.deleteFileByFileIds(fileIds);
    }

    /**
     * 删除文件上传信息
     * 
     * @param fileId 文件上传主键
     * @return 结果
     */
    @Override
    public int deleteFileByFileId(Long fileId)
    {
        return fileMapper.deleteFileByFileId(fileId);
    }

    @Override
    public File uploadFile(File file) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(File::getMd5, file.getMd5());

        File exist = getOne(wrapper);
        // 文件已存在, 直接返回
        if (exist != null) {
            return exist;
        }

        fileMapper.insert(file);
        return file;
    }
}
