package ru.stazaev.filestorage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stazaev.filestorage.entity.File;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByPath(String path);
}
