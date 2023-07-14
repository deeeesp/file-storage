package ru.stazaev.filestorage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.stazaev.filestorage.entity.enums.FileType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "files")
@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    private String path;

    @ManyToOne
    private User user;

}
