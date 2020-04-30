package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.snow.storeapi.entity.R;
import com.snow.storeapi.util.COSUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件、图片
 */

@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${COS.SECRET_ID}")
    private String SECRET_ID;

    @Value("${COS.SECRET_KEY}")
    private String SECRET_KEY;

    @Value("${COS.bucketName}")
    private String bucketName;

    @Value("${COS.FILE_HOST}")
    private String FILE_HOST;

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public Map upload(@RequestParam("file") MultipartFile multipartFile,@RequestParam("folder")String folder){
        String uploadUrl = null;
        if(multipartFile == null || multipartFile.isEmpty()){
            return R.error("文件不能为空");
        }

        String fileName = multipartFile.getOriginalFilename();
        long size = multipartFile.getSize();
        //图片大小限制为10M
        if(size > 10971520){
            return R.error("文件大小不能超过10M");
        }
        try {
            //上传到腾讯云COS
            if (StringUtils.isNotEmpty(fileName)) {
                String serverName = UUID.randomUUID().toString().replace("-", "");
                InputStream inputStream = multipartFile.getInputStream();
                String newServerName = COSUtil.changeFileName(fileName, serverName);
                newServerName = folder+"/" + newServerName;
                uploadUrl = COSUtil.upload(inputStream, newServerName, bucketName, FILE_HOST, SECRET_ID, SECRET_KEY);
            }
        }catch (Exception e){
            return R.error("上传失败，请重新上传");
        }
        return R.ok(uploadUrl);
    }

}
