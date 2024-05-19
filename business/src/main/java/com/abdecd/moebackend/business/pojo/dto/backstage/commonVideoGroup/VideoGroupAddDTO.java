package com.abdecd.moebackend.business.pojo.dto.backstage.commonVideoGroup;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
public class VideoGroupAddDTO {
    private String title;
    private String description;
    private MultipartFile cover;
    private ArrayList<String> tagIds;
}
