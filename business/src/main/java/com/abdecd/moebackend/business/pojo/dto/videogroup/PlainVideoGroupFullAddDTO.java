package com.abdecd.moebackend.business.pojo.dto.videogroup;

import com.abdecd.moebackend.common.constant.DTOConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PlainVideoGroupFullAddDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String cover;
    @NotBlank
    @Pattern(regexp = DTOConstant.TAGS_REGEXP)
    private String tags;
    @NotBlank
    String link;
}
