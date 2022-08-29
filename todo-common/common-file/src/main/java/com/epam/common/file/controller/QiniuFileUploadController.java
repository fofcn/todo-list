package com.epam.common.file.controller;

import com.epam.common.core.dto.SingleResponse;
import com.epam.common.file.service.QiniuFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public class QiniuFileUploadController {

    @Autowired
    private QiniuFileUploadService qiniuFileUploadService;

    @PostMapping
    public SingleResponse<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return SingleResponse.buildSuccess();
    }

    @DeleteMapping
    public SingleResponse deleteFile(String path) {
        return SingleResponse.buildSuccess();
    }
}
