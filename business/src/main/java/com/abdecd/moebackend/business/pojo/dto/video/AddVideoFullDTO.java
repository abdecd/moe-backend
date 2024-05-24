package com.abdecd.moebackend.business.pojo.dto.video;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class AddVideoFullDTO extends AddVideoDTO {
    @NotNull
    Byte videoStatusWillBe;
    @Nullable
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime videoPublishTime;
}
