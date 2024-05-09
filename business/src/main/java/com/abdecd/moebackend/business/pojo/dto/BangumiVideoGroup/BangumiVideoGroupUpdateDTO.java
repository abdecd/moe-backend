package com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
public class BangumiVideoGroupUpdateDTO {
    private Long id;
    private String title;
    private String description;
    private MultipartFile cover;
    private ArrayList<Integer> tagIds;
    private String releaseTime;
    private String updateAtAnnouncement;
    private String status;
}
