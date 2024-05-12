package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.PlainUserDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PlainUserDetailMapper extends BaseMapper<PlainUserDetail> {
    @Select("SELECT * FROM plain_user_detail WHERE user_id = #{id}")
    PlainUserDetail selectByUid(Long userId);
}
