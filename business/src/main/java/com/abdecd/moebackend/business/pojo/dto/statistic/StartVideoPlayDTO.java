package com.abdecd.moebackend.business.pojo.dto.statistic;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StartVideoPlayDTO {
    @NotNull
    private Long videoId;
}
