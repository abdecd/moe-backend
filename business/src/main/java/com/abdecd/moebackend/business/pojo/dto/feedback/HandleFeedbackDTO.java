package com.abdecd.moebackend.business.pojo.dto.feedback;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HandleFeedbackDTO {
    @NotNull
    private Long id;
}
