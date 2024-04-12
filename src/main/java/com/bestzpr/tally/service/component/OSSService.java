package com.bestzpr.tally.service.component;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @className OSSService
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/11 21:17
 * @Version 1.0
 */
@Slf4j
@Service
public class OSSService {

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {
        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        // 创建OSS客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 创建PutObjectRequest对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file.getInputStream());

            // 上传文件到OSS
            ossClient.putObject(putObjectRequest);
        } finally {
            // 关闭OSS客户端
            ossClient.shutdown();
        }

        // 返回文件访问URL
        return "https://" + bucketName + "." + endpoint + "/" + fileName;
    }

    public String uploadByteDataToOSS(String objectName, byte[] content) {
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 创建上传Object的Metadata
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setContentLength(content.length);
            // metadata.setContentType("image/png"); // 根据实际类型设置

            // 上传byte数组
            ossClient.putObject(new PutObjectRequest(bucketName, objectName, new ByteArrayInputStream(content)));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient
            ossClient.shutdown();
        }
        // 返回文件访问URL
        return "https://" + bucketName + "." + endpoint + "/" + objectName;
    }

}
