package com.abdecd.moebackend.business.pojo.dto.backstage.commonVideoGroup;

import io.micrometer.common.lang.Nullable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VideoGroupDTO {
    private Long id;
    @Nullable
    private String title;
    @Nullable
    private String description;
    @Nullable
    private MultipartFile cover;
    @Nullable
    private String tags;
    @Nullable
    private Byte videoGroupStatus;
    @Nullable
    private Double weight;
}
