package com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class BangumiVideoGroupAddDTO {
    private String title;
    private String description;
    private MultipartFile cover;
    private ArrayList<Integer> tagIds;
    private LocalDateTime releaseTime;
    private String updateAtAnnouncement;
    private String status;
}
