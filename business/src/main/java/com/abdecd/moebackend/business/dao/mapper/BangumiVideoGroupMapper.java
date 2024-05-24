package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface BangumiVideoGroupMapper extends BaseMapper<BangumiVideoGroup> {
    @Delete("DELETE FROM bangumi_video_group WHERE video_group_id = #{vid}")
    void deleteByVid(Long vid);

    void update_(BangumiVideoGroup bangumiVideoGroup);

    @Select("SELECT * FROM bangumi_video_group WHERE video_group_id = #{videoGroupId}")
    BangumiVideoGroup selectByVid(Long videoGroupId);

    @Select("""
                select bg.video_group_id
                from bangumi_video_group bg
                    join video_group vg on bg.video_group_id = vg.id
                where vg.status = #{status}
                order by bg.update_time desc
                limit #{num}
            """)
    List<Long> listIdsByUpdateTimeDesc(int num, Byte status);

    ArrayList<com.abdecd.moebackend.business.pojo.vo.backstage.videoGroup.BangumiVideoGroupVO> selectBangumiVideoGroupList(Integer pageIndex, Integer pageSize, String id, String title, Byte status);

    Integer selectBangumiVideoGroupListCount(String id, String title, Byte status);
}
