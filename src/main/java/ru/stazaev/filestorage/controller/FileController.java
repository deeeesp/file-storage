package ru.stazaev.filestorage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.stazaev.filestorage.dto.request.GetFileDto;
import ru.stazaev.filestorage.dto.request.SaveFileDto;
import ru.stazaev.filestorage.dto.response.FileDDto;
import ru.stazaev.filestorage.services.FileService;
import ru.stazaev.filestorage.services.PictureStorageService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/file/")
public class FileController {
    private final PictureStorageService pictureStorageService;
    private final FileService fileService;

    @GetMapping("get")
    public FileDDto getImage(@RequestBody GetFileDto fileDto) throws Exception {
        var a = fileService.getFile(fileDto);
        return a;
//        pictureStorageService.getPicture("1.png");
    }

    //@RequestParam - тоже можно, но делается через MultipartFile, как в saveImage
    @PostMapping("save")
    public void saveImage(@RequestBody MultipartFile file,
                          @RequestBody String username) throws Exception {
        System.out.println(username);
//        pictureStorageService.savePicture(file.getOriginalFilename(), file);
    }


    //тут нужно именно @ModelAttribute, а не @RequestBody (с ним не работает)
    @PostMapping("dto")
    public void saveImage2(
            @ModelAttribute SaveFileDto saveFileDto,
            @ModelAttribute("username") String username) throws Exception {
        System.out.println(username);
        fileService.saveFile(saveFileDto, username);
//        pictureStorageService.savePicture(fileDto, username);

    }

    @PostMapping("delete")
    public void deleteFile(@RequestParam("path") String path,
                           @RequestParam("username") String username) throws Exception {
        fileService.deleteFile(path,username);
//        pictureStorageService.deletePicture(name);
    }
}
