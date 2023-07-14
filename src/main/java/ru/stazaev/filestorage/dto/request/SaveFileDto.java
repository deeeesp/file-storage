package ru.stazaev.filestorage.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.stazaev.filestorage.entity.enums.FileType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveFileDto {
    private FileType fileType;
    private MultipartFile file;
    private String path;
}
