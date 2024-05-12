package com.abdecd.moebackend.business.pojo.dto.video;

import com.abdecd.moebackend.business.dao.entity.Video;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
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
    @Schema(description = "完整链接")
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
