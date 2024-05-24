package com.abdecd.moebackend.business.pojo.dto.video;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class UpdateVideoFullDTO extends UpdateVideoDTO {
    @Nullable
    Byte videoStatusWillBe;
    @Nullable
    LocalDateTime videoPublishTime;
}
