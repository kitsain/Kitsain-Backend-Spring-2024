package com.myblogbackend.blog.services;

import com.myblogbackend.blog.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MinioService {
    boolean bucketExists(String bucketName);

    void makeBucket(String bucketName);

    FileResponse putObject(MultipartFile multipartFile, String bucketName, String fileType);

    List<FileResponse> putObjects(MultipartFile[] multipartFile, String bucketName, String fileType);

}
