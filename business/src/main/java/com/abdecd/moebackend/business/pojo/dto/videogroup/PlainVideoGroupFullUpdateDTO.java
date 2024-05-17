package com.abdecd.moebackend.business.pojo.dto.videogroup;

import com.abdecd.moebackend.common.constant.DTOConstant;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PlainVideoGroupFullUpdateDTO {
    @NotNull
    private Long id;
    private String title;
    private String description;
    private String cover;
    @Nullable
    @Pattern(regexp = DTOConstant.TAGS_REGEXP)
    private String tags;
    String link;
}
