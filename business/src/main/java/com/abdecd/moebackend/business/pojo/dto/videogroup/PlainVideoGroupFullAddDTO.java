package com.abdecd.moebackend.business.pojo.dto.videogroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlainVideoGroupFullAddDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String cover;
    @NotNull
    private Long[] tagIds;
    @NotBlank
    String link;
}
