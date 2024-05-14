package com.abdecd.moebackend.business.pojo.dto.videogroup;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeDTO {
    @NotNull
    private Long id;
    @NotNull
    private Byte status;
}
