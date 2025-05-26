package per.chowhound.hfut.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.io.FileUtil;
import io.minio.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.web.multipart.MultipartFile;
import per.chowhound.hfut.domain.File;
import per.chowhound.hfut.service.IFileService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 文件上传Controller
 * 
 * @author chowhound
 * @date 2025-03-19
 */
@RestController
@RequestMapping("/file/file")
public class FileController extends BaseController
{
    public static final String BUCKET_NAME = "automotive-defect";
    public static final String FILE_URL = "http://nas.com:9001/" + BUCKET_NAME + "/";
    @Autowired
    private IFileService fileService;
    @Autowired
    private MinioClient minioClient;


    /**
     * 查询文件上传列表
     */
    @PreAuthorize("@ss.hasPermi('file:file:list')")
    @GetMapping("/list")
    public TableDataInfo list(File file)
    {
        startPage();
        List<File> list = fileService.selectFileList(file);
        return getDataTable(list);
    }

    /**
     * 导出文件上传列表
     */
    @PreAuthorize("@ss.hasPermi('file:file:export')")
    @Log(title = "文件上传", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, File file)
    {
        List<File> list = fileService.selectFileList(file);
        ExcelUtil<File> util = new ExcelUtil<File>(File.class);
        util.exportExcel(response, list, "文件上传数据");
    }

    /**
     * 获取文件上传详细信息
     */
    @PreAuthorize("@ss.hasPermi('file:file:query')")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId)
    {
        return success(fileService.selectFileByFileId(fileId));
    }

    /**
     * 新增文件上传
     */
    @Log(title = "文件上传", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestParam("file")MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return error("Please select a file to upload.");
        }
        byte[] bytes = file.getBytes();
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
        }
        java.io.File tempFile = FileUtil.createTempFile();
        Files.write(tempFile.toPath(), bytes);
        ObjectWriteResponse response = minioClient.uploadObject(UploadObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(UUID.randomUUID() + file.getOriginalFilename())
                .filename(tempFile.getAbsolutePath()).build());

        File fileRep = new File();
        fileRep.setUrl(FILE_URL + response.object());
        fileRep.setMd5(DigestUtils.md5DigestAsHex(bytes));
        fileRep.setName(file.getOriginalFilename());
        fileRep.setType(response.object().substring(response.object().lastIndexOf(".") + 1));

        return success(fileService.uploadFile(fileRep));

//        return toAjax(fileService.insertFile(file));
    }

    /**
     * 修改文件上传
     */
    @PreAuthorize("@ss.hasPermi('file:file:edit')")
    @Log(title = "文件上传", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody File file)
    {
        return toAjax(fileService.updateFile(file));
    }

    /**
     * 删除文件上传
     */
    @PreAuthorize("@ss.hasPermi('file:file:remove')")
    @Log(title = "文件上传", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable Long[] fileIds)
    {
        return toAjax(fileService.deleteFileByFileIds(fileIds));
    }
}
