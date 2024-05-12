package com.abdecd.moebackend.business.pojo.dto.videogroup;

import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlainVideoGroupUpdateDTO {
    @NotNull
    private Long id;
    private String title;
    private String description;
    private String cover;
    private Long[] tagIds;

    public VideoGroup toEntity(Long userId) {
        return new VideoGroup()
                .setId(id)
                .setUserId(userId)
                .setTitle(title)
                .setCover(cover)
                .setDescription(description)
                .setType(VideoGroup.Type.PLAIN_VIDEO_GROUP);
    }
}
