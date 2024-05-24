package com.abdecd.moebackend.business.pojo.dto.video;

import com.abdecd.moebackend.business.dao.entity.Video;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class UpdateVideoFullDTO extends UpdateVideoDTO {
    @Nullable
    Byte videoStatusWillBe = Video.Status.ENABLE;
    @Nullable
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime videoPublishTime;
}
