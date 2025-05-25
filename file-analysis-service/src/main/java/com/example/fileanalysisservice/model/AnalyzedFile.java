package com.example.fileanalysisservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "analyzed_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzedFile {

    @Id
    private UUID fileId;

    private String location;

}
