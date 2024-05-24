package com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup;

import jakarta.annotation.Nullable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BangumiVideoGroupUpdateDTO {
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
    private String releaseTime;
    @Nullable
    private String updateAtAnnouncement;
    @Nullable
    private String status;
    @Nullable
    private String videoGroupStatus;
    @Nullable
    private Double weight;
}
