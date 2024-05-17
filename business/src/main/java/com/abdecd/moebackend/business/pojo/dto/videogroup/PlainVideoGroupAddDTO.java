package com.abdecd.moebackend.business.pojo.dto.videogroup;

import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.common.constant.DTOConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlainVideoGroupAddDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String cover;
    @NotBlank
    @Pattern(regexp = DTOConstant.TAGS_REGEXP)
    private String tags;

    public VideoGroup toEntity(Long userId) {
        return new VideoGroup()
                .setUserId(userId)
                .setTitle(title)
                .setCover(cover)
                .setDescription(description)
                .setCreateTime(LocalDateTime.now())
                .setType(VideoGroup.Type.PLAIN_VIDEO_GROUP)
                .setTags(tags)
                .setWeight(1.0)
                .setVideoGroupStatus(VideoGroup.Status.TRANSFORMING);
    }
}
