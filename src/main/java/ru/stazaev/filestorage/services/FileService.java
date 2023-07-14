package ru.stazaev.filestorage.services;

import ru.stazaev.filestorage.dto.request.GetFileDto;
import ru.stazaev.filestorage.dto.request.SaveFileDto;
import ru.stazaev.filestorage.dto.response.FileDDto;
import ru.stazaev.filestorage.entity.File;

public interface FileService {
    void saveFile(SaveFileDto saveFileDto, String username);

    void deleteFile(String filepath, String username);

    FileDDto getFile(GetFileDto fileDto);

}
