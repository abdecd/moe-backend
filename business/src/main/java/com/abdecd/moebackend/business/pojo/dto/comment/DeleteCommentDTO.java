package com.abdecd.moebackend.business.pojo.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteCommentDTO {
    @NotNull
    @Schema(description = "评论id")
    private Long id;
}
