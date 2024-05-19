package com.abdecd.moebackend.business.pojo.dto.feedback;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddFeedbackDTO {
    @NotBlank
    private String content;

    @NotBlank
    @Email
    private String email;
}
