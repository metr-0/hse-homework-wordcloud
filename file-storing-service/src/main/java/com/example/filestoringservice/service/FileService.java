package com.example.filestoringservice.service;

import com.example.filestoringservice.dto.FileInfo;
import com.example.filestoringservice.model.StoredFile;
import com.example.filestoringservice.repository.FileRepository;
import com.example.filestoringservice.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final HashService hashService;
    private final FileStorage fileStorage;

    public UUID storeFile(MultipartFile file) {
        try {
            String hash = hashService.computeHash(file.getInputStream());

            return fileRepository.findByHash(hash)
                    .map(StoredFile::getId)
                    .orElseGet(() -> {
                        try {
                            String location = fileStorage.saveFile(file,
                                    UUID.randomUUID() + "_" + file.getOriginalFilename());
                            StoredFile stored = new StoredFile();
                            stored.setHash(hash);
                            stored.setName(file.getOriginalFilename());
                            stored.setLocation(location);
                            stored = fileRepository.save(stored);
                            return stored.getId();
                        } catch (IOException e) {
                            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                    "Failed to create file: " + e.getMessage(), e);
                        }
                    });
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to compute hash: " + e.getMessage(), e);
        }
    }

    public FileInfo getFile(UUID id) {
        StoredFile file = fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new FileInfo(
                file.getName(),
                fileStorage.loadFile(file.getLocation())
        );
    }

    public List<UUID> getAllFileIds() {
        return fileRepository.findAll().stream()
                .map(StoredFile::getId)
                .toList();
    }

    public void deleteFile(UUID id) {
        StoredFile file = fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        try {
            fileStorage.deleteFile(file.getLocation());
            fileRepository.delete(file);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to delete file: " + e.getMessage(), e);
        }
    }

}
