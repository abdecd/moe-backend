package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.pojo.dto.comment.AddCommentDTO;
import com.abdecd.moebackend.business.pojo.vo.comment.UserCommentVO;
import com.abdecd.moebackend.common.result.PageVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    PageVO<List<UserCommentVO>> getComment(Long videoId, Integer page, Integer pageSize);

    Long addComment(AddCommentDTO addCommentDTO);

    void deleteComment(Long id);
    Long getCommentCount(Long videoId);
}
