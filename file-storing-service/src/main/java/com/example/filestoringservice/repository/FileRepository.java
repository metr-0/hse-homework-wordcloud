package com.example.filestoringservice.repository;

import com.example.filestoringservice.model.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<StoredFile, UUID> {

    Optional<StoredFile> findByHash(String hash);

}
