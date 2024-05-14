package com.abdecd.moebackend.business.pojo.dto.videogroup;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeDTO {
    @NotNull
    private Long id;
    @NotNull
    @Min(0)
    @Max(1)
    private Byte status;
}
