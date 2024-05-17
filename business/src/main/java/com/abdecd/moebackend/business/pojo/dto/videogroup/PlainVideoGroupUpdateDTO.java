package com.abdecd.moebackend.business.pojo.dto.videogroup;

import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.common.constant.DTOConstant;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PlainVideoGroupUpdateDTO {
    @NotNull
    private Long id;
    private String title;
    private String description;
    private String cover;
    @Nullable
    @Pattern(regexp = DTOConstant.TAGS_REGEXP)
    private String tags;

    public VideoGroup toEntity(Long userId) {
        return new VideoGroup()
                .setId(id)
                .setUserId(userId)
                .setTitle(title)
                .setCover(cover)
                .setTags(tags)
                .setDescription(description)
                .setType(VideoGroup.Type.PLAIN_VIDEO_GROUP);
    }
}
