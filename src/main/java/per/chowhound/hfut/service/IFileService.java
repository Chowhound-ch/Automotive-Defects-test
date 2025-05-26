package per.chowhound.hfut.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import per.chowhound.hfut.domain.File;

/**
 * 文件上传Service接口
 * 
 * @author chowhound
 * @date 2025-03-19
 */
public interface IFileService extends IService<File> {
    /**
     * 查询文件上传
     * 
     * @param fileId 文件上传主键
     * @return 文件上传
     */
    public File selectFileByFileId(Long fileId);

    /**
     * 查询文件上传列表
     * 
     * @param file 文件上传
     * @return 文件上传集合
     */
    public List<File> selectFileList(File file);

    /**
     * 新增文件上传
     * 
     * @param file 文件上传
     * @return 结果
     */
    public int insertFile(File file);

    /**
     * 修改文件上传
     * 
     * @param file 文件上传
     * @return 结果
     */
    public int updateFile(File file);

    /**
     * 批量删除文件上传
     * 
     * @param fileIds 需要删除的文件上传主键集合
     * @return 结果
     */
    public int deleteFileByFileIds(Long[] fileIds);

    /**
     * 删除文件上传信息
     * 
     * @param fileId 文件上传主键
     * @return 结果
     */
    public int deleteFileByFileId(Long fileId);

    File uploadFile(File file);
}
