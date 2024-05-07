package com.abdecd.moebackend.business.pojo.dto.video;

import com.abdecd.moebackend.business.dao.entity.Video;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateVideoDTO {
    @NotNull
    Long id;
    Long videoGroupId;
    Integer index;
    String title;
    String description;
    String cover;
    String link;

    public Video toEntity() {
        var video = new Video()
                .setId(id)
                .setVideoGroupId(videoGroupId)
                .setIndex(index)
                .setTitle(title)
                .setDescription(description)
                .setCover(cover)
                .setLink(link);
        if (link != null) video = video.setStatus(Video.Status.TRANSFORMING);
        return video;
    }
}
