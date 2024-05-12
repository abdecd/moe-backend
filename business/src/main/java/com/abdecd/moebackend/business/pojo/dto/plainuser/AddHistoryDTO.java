package com.abdecd.moebackend.business.pojo.dto.plainuser;

import com.abdecd.moebackend.business.dao.entity.PlainUserHistory;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class AddHistoryDTO {
    @NotNull
    private Long videoId;

    public PlainUserHistory toEntity(Long userId, Long videoGroupId) {
        return new PlainUserHistory()
                .setUserId(userId)
                .setVideoGroupId(videoGroupId)
                .setVideoId(videoId)
                .setTimestamp(LocalDateTime.now());
    }
}
