package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.UserComment;
import com.abdecd.moebackend.business.pojo.vo.comment.UserCommentVOBasic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserCommentMapper extends BaseMapper<UserComment> {
    List<UserCommentVOBasic> listCommentBySthId(Long videoId, Byte status);
    Integer countRootCommentBySthId(Long videoId, Byte status);
}
