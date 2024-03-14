package com.myblogbackend.blog.services.impl;


import com.myblogbackend.blog.config.minio.MinioConfig;
import com.myblogbackend.blog.exception.MinioOperationException;
import com.myblogbackend.blog.response.FileResponse;
import com.myblogbackend.blog.services.MinioService;
import com.myblogbackend.blog.utils.MinioUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final static Logger LOGGER = LogManager.getLogger(MinioServiceImpl.class);
    private final MinioUtil minioUtil;
    private final MinioConfig minioConfig;

    @SneakyThrows
    @Override
    public FileResponse putObject(final MultipartFile multipartFile,
                                  final String bucket, final String fileType) {
        LOGGER.info("we are starting the execution of findAll  () in service ");
        LOGGER.info("MinioServiceImpl | putObject is called");
        try {
            var bucketName = StringUtils.defaultIfBlank(bucket, minioConfig.getBucketName());
            createBucketIfNotExists(bucketName);
            String fileName = multipartFile.getOriginalFilename();
            String objectName = generateObjectName(fileName);
            LocalDateTime createdTime = LocalDateTime.now();
            minioUtil.putObject(bucketName, multipartFile, objectName, fileType);
            LOGGER.info("MinioServiceImpl | putObject | url : " + buildObjectUrl(bucketName, objectName));
            return buildFileResponse(bucketName, objectName, multipartFile.getSize(), fileType, createdTime);
        } catch (Exception e) {
            LOGGER.error("MinioServiceImpl | putObject | Exception : " + e.getMessage());
            throw new MinioOperationException("Error putting object to Minio", e);
        }
    }

    @SneakyThrows
    @Override
    public List<FileResponse> putObjects(final MultipartFile[] multipartFiles,
                                         final String bucket, final String fileType) {
        var bucketName = StringUtils.defaultIfBlank(bucket, minioConfig.getBucketName());
        createBucketIfNotExists(bucketName);
        List<FileResponse> files = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            FileResponse fileResponse = processFile(file, bucketName, fileType);
            files.add(fileResponse);
        }

        return files;
    }

    private FileResponse processFile(final MultipartFile file, final
    String bucketName, final String fileType) {
        String fileName = file.getOriginalFilename();
        String objectName = generateObjectName(fileName);
        LocalDateTime createdTime = LocalDateTime.now();

        minioUtil.putObject(bucketName, file, objectName, fileType);

        return buildFileResponse(bucketName, objectName, file.getSize(), fileType, createdTime);
    }

    private void createBucketIfNotExists(final String bucketName) {
        if (!bucketExists(bucketName)) {
            makeBucket(bucketName);
            LOGGER.info("MinioServiceImpl | createBucketIfNotExists | bucketName : " + bucketName + " created");
        }
    }

    private String generateObjectName(final String fileName) {
        return UUID.randomUUID().toString().replaceAll("-", "")
                + StringUtils.defaultString(fileName).substring(StringUtils.defaultString(fileName).lastIndexOf("."));
    }

    private FileResponse buildFileResponse(final String bucketName, final String objectName, final long fileSize,
                                           final String fileType, final LocalDateTime createdTime) {
        return FileResponse.builder()
                .filename(objectName)
                .fileSize(fileSize)
                .contentType(fileType)
                .createdTime(createdTime)
                .build();
    }

    private String buildObjectUrl(final String bucketName, final String objectName) {
        return minioConfig.getEndpoint() + "/" + bucketName + "/" + objectName;
    }

    @Override
    public boolean bucketExists(final String bucketName) {
        LOGGER.info("MinioServiceImpl | bucketExists is called");
        return minioUtil.bucketExists(bucketName);
    }

    @Override
    public void makeBucket(final String bucketName) {
        LOGGER.info("MinioServiceImpl | makeBucket is called");
        LOGGER.info("MinioServiceImpl | makeBucket | bucketName : " + bucketName);
        minioUtil.makeBucket(bucketName);
    }
}
