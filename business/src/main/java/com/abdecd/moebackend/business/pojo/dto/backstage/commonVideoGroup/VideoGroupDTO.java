package com.abdecd.moebackend.business.pojo.dto.backstage.commonVideoGroup;

import io.micrometer.common.lang.Nullable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

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
    private String date;
    @Nullable
    private ArrayList<String> tagIds;
}
