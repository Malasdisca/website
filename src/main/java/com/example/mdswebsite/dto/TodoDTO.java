package com.example.mdswebsite.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor

public class TodoDTO {
    // Common fields
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private OffsetDateTime dueAt;
    private Long teamId;
    private List<String> tags;
    private Integer orderIndex;

    // For create
    public record Create(
            @NotBlank String title,
            String description,
            String status,
            String priority,
            OffsetDateTime dueAt,
            Long teamId,
            List<String> tags
    ) {}

    // For update
    public record Update(
            String title,
            String description,
            String status,
            String priority,
            OffsetDateTime dueAt,
            List<String> tags
    ) {}

    // For reorder
    public record Reorder(
            @NotEmpty List<Long> ids,
            int orderIndexStart
    ) {}

    // For response
    public record Response(
            Long id,
            String title,
            String description,
            String status,
            String priority,
            OffsetDateTime dueAt,
            Long teamId,
            List<String> tags,
            int orderIndex
    ) {}
}
