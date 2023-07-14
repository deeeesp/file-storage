package ru.stazaev.filestorage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.stazaev.filestorage.entity.enums.FileType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDDto {
    private FileType fileType;
    private byte[] bytes;
}
