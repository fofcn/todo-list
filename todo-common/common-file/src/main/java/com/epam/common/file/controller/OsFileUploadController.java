package com.epam.common.file.controller;

import com.epam.common.core.dto.SingleResponse;
import com.epam.common.file.service.OsFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file/upload/os")
public class OsFileUploadController {

    @Autowired
    private OsFileUploadService osFileUploadService;

    @PostMapping
    public SingleResponse<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return SingleResponse.buildSuccess();
    }

    @DeleteMapping
    public SingleResponse deleteFile(String path) {
        return SingleResponse.buildSuccess();
    }
}
