package ru.stazaev.filestorage.services;

import org.springframework.web.multipart.MultipartFile;

public interface PictureStorageService {
    void savePicture(String name, MultipartFile pictureFile) throws Exception;
    byte[] getPicture(String name) throws Exception;
    void deletePicture(String name) throws Exception;
}
