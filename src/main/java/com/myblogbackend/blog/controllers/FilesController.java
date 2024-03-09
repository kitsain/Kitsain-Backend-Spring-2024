package com.myblogbackend.blog.controllers;

import com.myblogbackend.blog.config.minio.FileResponseException;
import com.myblogbackend.blog.controllers.route.CommonRoutes;
import com.myblogbackend.blog.response.FileResponse;
import com.myblogbackend.blog.services.MinioService;
import com.myblogbackend.blog.utils.FileTypeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(CommonRoutes.BASE_API + CommonRoutes.VERSION + "/files")
@RequiredArgsConstructor
public class FilesController {
    private static final Logger LOGGER = LogManager.getLogger(FilesController.class);

    private final MinioService minioService;

    @PostMapping("/upload")
    public FileResponse uploadFile(final MultipartFile file, final String bucketName) {
        LOGGER.info("MinioController | uploadFile is called");
        LOGGER.info("MinioController | uploadFile | bucketName : " + bucketName);
        String fileType = FileTypeUtils.getFileType(file);
        LOGGER.info("MinioController | uploadFile | fileType : " + fileType);
        if (fileType != null) {
            return minioService.putObject(file, bucketName, fileType);
        }
        throw new FileResponseException("File cannot be Upload");
    }

    @PostMapping("/upload/files")
    public List<FileResponse> uploadFiles(final MultipartFile[] files, final String bucketName) {
        String fileType = "";
        for (MultipartFile file : files) {
            fileType = FileTypeUtils.getFileType(file);
        }
        LOGGER.info("MinioController | uploadFile | fileType : " + fileType);
        if (fileType != null) {
            return minioService.putObjects(files, bucketName, fileType);
        }
        throw new FileResponseException("File cannot be Upload");
    }
}
