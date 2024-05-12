package com.abdecd.moebackend.business.pojo.dto.videogroup;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlainVideoGroupFullUpdateDTO {
    @NotNull
    private Long id;
    private String title;
    private String description;
    private String cover;
    private Long[] tagIds;
    String link;
}
