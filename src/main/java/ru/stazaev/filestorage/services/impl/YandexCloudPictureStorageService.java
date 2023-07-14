package ru.stazaev.filestorage.services.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.stazaev.filestorage.services.PictureStorageService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class YandexCloudPictureStorageService implements PictureStorageService {


    @Value("${yandex.cloud.storage.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    @Override
    public void savePicture(String name, MultipartFile pictureFile) throws IOException, AmazonClientException {
        s3Client.putObject(bucketName, name, pictureFile.getInputStream(), new ObjectMetadata());
    }

    @Override
    public byte[] getPicture(String name) throws IOException, AmazonClientException {
        S3Object s3Object = s3Client.getObject(bucketName, name);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        return IOUtils.toByteArray(inputStream);
    }

    @Override
    public void deletePicture(String name) throws AmazonClientException {
        s3Client.deleteObject(bucketName, name);
    }

}
