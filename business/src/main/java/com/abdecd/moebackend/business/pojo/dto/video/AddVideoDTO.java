package com.abdecd.moebackend.business.pojo.dto.video;

import com.abdecd.moebackend.business.dao.entity.Video;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddVideoDTO {
    @NotNull
    Long videoGroupId;
    @NotNull
    Integer index;
    @NotBlank
    String title;
    @NotBlank
    String description;
    @NotBlank
    String cover;
    @NotBlank
    String link;

    public Video toEntity() {
        return new Video()
                .setVideoGroupId(videoGroupId)
                .setIndex(index)
                .setTitle(title)
                .setDescription(description)
                .setCover(cover)
                .setUploadTime(LocalDateTime.now())
                .setStatus(Video.Status.TRANSFORMING);
    }
}
