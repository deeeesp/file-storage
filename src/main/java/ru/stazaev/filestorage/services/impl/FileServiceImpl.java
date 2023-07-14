package ru.stazaev.filestorage.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.stazaev.filestorage.dto.request.GetFileDto;
import ru.stazaev.filestorage.dto.request.SaveFileDto;
import ru.stazaev.filestorage.dto.response.FileDDto;
import ru.stazaev.filestorage.entity.File;
import ru.stazaev.filestorage.entity.User;
import ru.stazaev.filestorage.entity.enums.Role;
import ru.stazaev.filestorage.repository.FileRepository;
import ru.stazaev.filestorage.repository.UserRepository;
import ru.stazaev.filestorage.services.FileService;
import ru.stazaev.filestorage.services.PictureStorageService;
import java.util.NoSuchElementException;

@Log4j2
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final PictureStorageService pictureStorageService;
    private final ModelMapper modelMapper;

    @Override
    public void saveFile(SaveFileDto saveFileDto, String username) {
        var user = findUserByUsername(username);
        var fileName = saveFileDto.getPath();
        if (fileRepository.findByPath(fileName).isPresent()){
            throw new RuntimeException("Файл с таким названием уже существует");
        }
        var file = modelMapper.map(saveFileDto, File.class);
        file.setUser(user);
        fileRepository.save(file);
        log.info("Пользователь " + username + " сохранил файл " + fileName);
        try {
            pictureStorageService.savePicture(fileName, saveFileDto.getFile());
        } catch (Exception e) {
            log.error("Не удалось сохранить файл в yandex cloud" + fileName + " " + username);
        }
    }

    @Override
    public void deleteFile(String filepath, String username) {
        var file = findFileByPath(filepath);
        var user = findUserByUsername(username);
        if (!file.getUser().equals(user) && user.getRole().equals(Role.USER)){
            throw new RuntimeException("Недостаточно прав");
        }
        try {
            pictureStorageService.deletePicture(filepath);
        }catch (Exception e){
            log.error("Не удалось сохранить файл в yandex cloud" + filepath + " " + username);
            return;
        }
        user.getFileList().remove(file);

        fileRepository.delete(file);
        userRepository.save(user);
    }

    @Override
    public FileDDto getFile(GetFileDto fileDto) {
        var user = findUserByUsername(fileDto.getUsername());
        var file = findFileByPath(fileDto.getPath());
        if (!file.getUser().equals(user)){
            throw new RuntimeException("Недостаточно прав");
        }
        try {

            return FileDDto.builder()
                    .fileType(file.getFileType())
                    .bytes(pictureStorageService.getPicture(file.getPath()))
                    .build();
        }catch (Exception e){
            log.error("Не удалось получить файл");
            throw new RuntimeException("Не удалось получить файл");
        }
    }


    private User findUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchElementException("Пользователь с таким именем не найден"));
    }

    private File findFileByPath(String path){
        return fileRepository.findByPath(path).orElseThrow(
                () -> new NoSuchElementException("Файл с таким путем не найден"));
    }
}
