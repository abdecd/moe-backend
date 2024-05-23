package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.Feedback;

import com.abdecd.moebackend.business.pojo.vo.feedback.FeedbackVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
    Page<FeedbackVO> selectFeedbacks(Page<?> page, @Param("email") String email, @Param("content") String content);
}
