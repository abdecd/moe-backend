package com.abdecd.moebackend.business.pojo.dto.feedback;


import com.abdecd.moebackend.common.constant.DTOConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AddFeedbackDTO {
    @NotBlank
    @Length(max = DTOConstant.FEEDBACK_LENGTH_MAX)
    private String content;

    @NotBlank
    @Email
    private String email;
}
