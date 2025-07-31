package com.serena.ossservice.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.serena.ossservice.util.OssPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public String upload(MultipartFile file) {

            //based on region
            String endpoint = OssPropertiesUtil.ENDPOINT;

            String accessKeyId = OssPropertiesUtil.ACCESS_KEY_ID;
            String accessKeySecret = OssPropertiesUtil.SECRET;
            String bucket = OssPropertiesUtil.BUCKET;

        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //upload file stream
            InputStream inputStream = null;
            inputStream = file.getInputStream();



            // 01.jpg
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileName = uuid + file.getOriginalFilename();

            //create folder with current date and upload to oss
            // 2025/02/02/01.jpg
            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            fileName = timeUrl + "/" + fileName;

            ossClient.putObject(bucket, fileName, inputStream);
            ossClient.shutdown();

            String url = "https://" + bucket + "." + endpoint + "/" + fileName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
