package com.abdecd.moebackend.business.pojo.vo.feedback;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FeedbackVO {
    private Long id;
    private String content;
    private String email;
    private LocalDateTime timestamp;
    private Integer status;
}
