package com.abdecd.moebackend.business.pojo.dto.videogroup;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlainVideoGroupDeleteDTO {
    @NotNull
    private Long id;
}
