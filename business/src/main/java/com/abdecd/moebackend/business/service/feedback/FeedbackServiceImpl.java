package com.abdecd.moebackend.business.service.feedback;


import com.abdecd.moebackend.business.dao.entity.Feedback;
import com.abdecd.moebackend.business.dao.mapper.FeedbackMapper;
import com.abdecd.moebackend.business.pojo.dto.feedback.AddFeedbackDTO;
import com.abdecd.moebackend.business.service.feedback.FeedbackService;
import com.abdecd.moebackend.common.constant.StatusConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public Long addFeedback(AddFeedbackDTO addFeedbackDTO) {
        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(addFeedbackDTO, feedback);
        feedback.setTimestamp(LocalDateTime.now());
        feedback.setStatus(Integer.valueOf(StatusConstant.ENABLE)); // 设置初始状态为等待处理
        feedbackMapper.insert(feedback);
        return feedback.getId();
    }
}
