package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.snow.storeapi.util.COSUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @Value("${TENCENTCLOUD.SECRET_ID}")
    private String SECRET_ID;

    @Value("${TENCENTCLOUD.SECRET_KEY}")
    private String SECRET_KEY;

    @Value("${COS.bucketName}")
    private String bucketName;

    @Value("${COS.FILE_HOST}")
    private String FILE_HOST;

    @ApiOperation("上传文件")
    @PostMapping("/upload/{folder}")
    public ResponseEntity upload(@RequestParam("file") MultipartFile multipartFile, @PathVariable String folder){
        Map<String, Object> res = new HashMap<>();
        String uploadUrl = null;
        if(multipartFile == null || multipartFile.isEmpty()){
            res.put("msg","文件不能为空!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        String fileName = multipartFile.getOriginalFilename();
        long size = multipartFile.getSize();
        //图片大小限制为10M
        if(size > 5 * 1024 * 1024){
            res.put("msg","文件大小不能超过10M!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        try {
            //上传到腾讯云COS
            if (StrUtil.isNotEmpty(fileName)) {
                String serverName = UUID.randomUUID().toString().replace("-", "");
                InputStream inputStream = multipartFile.getInputStream();
                String newServerName = COSUtil.changeFileName(fileName, serverName);
                newServerName = folder+"/" + newServerName;
                uploadUrl = COSUtil.upload(inputStream, newServerName, bucketName, FILE_HOST, SECRET_ID, SECRET_KEY);
            }
        }catch (Exception e){
            res.put("msg","上传失败，请重新上传!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        res.put("result", uploadUrl);
        return ResponseEntity.ok(res);
    }

}
