package com.snow.storeapi.util;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.snow.storeapi.exception.CustomException;

import java.io.InputStream;

public class COSUtil {
    public COSUtil() {
    }

    public static String upload(InputStream inputStream, String servername, String bucketName, String FILE_HOST, String SECRET_ID, String SECRET_KEY) {
        COSCredentials cred = new BasicCOSCredentials(SECRET_ID, SECRET_KEY);
        Region region = new Region("ap-beijing");
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        new PutObjectResult();

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, servername, inputStream, (ObjectMetadata) null);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            if (null != putObjectResult) {
                String var12 = FILE_HOST + "/" + servername;
                return var12;
            }
        } catch (CosServiceException var17) {
            throw new CustomException("上传图片出错,cosService出错");
        } catch (CosClientException var18) {
            throw new CustomException("上传图片出错，cosClient出错");
        } finally {
            cosClient.shutdown();
        }

        return null;
    }

    public static String changeFileName(String oldName, String newName) {
        oldName = oldName.substring(oldName.lastIndexOf("."));
        return newName + oldName;
    }

    public static void deleteSingleFile(String newServerName, String bucketName, String SECRET_ID, String SECRET_KEY) {
        COSCredentials cred = new BasicCOSCredentials(SECRET_ID, SECRET_KEY);
        Region region = new Region("ap-shanghai");
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);

        try {
            cosClient.deleteObject(bucketName, newServerName);
        } catch (CosServiceException var13) {
            throw new CustomException("上传图片出错,cosService出错");
        } catch (CosClientException var14) {
            throw new CustomException("上传图片出错，cosClient出错");
        } finally {
            cosClient.shutdown();
        }

    }

    public static InputStream downloadFile(String serverName, String bucketName, String SECRET_ID, String SECRET_KEY) {
        COSCredentials cred = new BasicCOSCredentials(SECRET_ID, SECRET_KEY);
        Region region = new Region("ap-shanghai");
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);

        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, serverName);
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            COSObjectInputStream cosObjectInputStream = cosObject.getObjectContent();
            return cosObjectInputStream;
        } catch (Exception var11) {
            throw new CustomException("下载文件失败");
        }
    }
}
