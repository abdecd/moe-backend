package com.abdecd.moebackend.business.pojo.dto.bangumiVideoGroup;

import jakarta.annotation.Nullable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

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
    private ArrayList<Integer> tagIds;
    @Nullable
    private String releaseTime;
    @Nullable
    private String updateAtAnnouncement;
    @Nullable
    private String status;
}
