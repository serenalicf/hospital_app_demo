package com.serena.ossservice.controller;

import com.serena.commonutil.result.Result;
import com.serena.ossservice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {

    @Autowired
    private FileService fileService;

    //upload file to Aliyun OSS
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) {
        //get upload file
        String url = fileService.upload(file);
        return Result.ok(url);

    }

}
