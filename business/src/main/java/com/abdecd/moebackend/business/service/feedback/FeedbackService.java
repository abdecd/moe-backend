package com.abdecd.moebackend.business.service.feedback;


import com.abdecd.moebackend.business.pojo.dto.feedback.AddFeedbackDTO;

public interface FeedbackService {
    Long addFeedback(AddFeedbackDTO addFeedbackDTO);
}
