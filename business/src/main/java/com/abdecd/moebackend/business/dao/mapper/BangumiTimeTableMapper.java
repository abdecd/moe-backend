package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.BangumiTimeTable;
import com.abdecd.moebackend.business.pojo.vo.videogroup.BangumiTimeTableBackVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BangumiTimeTableMapper extends BaseMapper<BangumiTimeTable> {
    Page<BangumiTimeTableBackVO> pageBangumiTimeTable(Page<BangumiTimeTableBackVO> page);
}
