package com.example.fileanalysisservice.repository;

import com.example.fileanalysisservice.model.AnalyzedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnalyzedFileRepository extends JpaRepository<AnalyzedFile, UUID> {
}
