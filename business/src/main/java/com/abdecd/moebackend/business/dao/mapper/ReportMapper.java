package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.Report;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;

@Mapper
public interface ReportMapper extends BaseMapper<Report> {
    @Update("UPDATE report SET status = 0 WHERE id = #{id}")
    void auditReport(Long id);

    @Select("SELECT * FROM report WHERE type = 0 ORDER BY type LIMIT #{pageSize} OFFSET #{offset}")
    ArrayList<Report> getVideoReportPage(Integer offset, Integer pageSize);

    @Select("SELECT * FROM report WHERE type = 1 ORDER BY type LIMIT #{pageSize} OFFSET #{offset}")
    ArrayList<Report> getCommentReportPage(Integer offset, Integer pageSize);
}
