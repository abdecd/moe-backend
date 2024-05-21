package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.PlainUserFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlainUserFavoriteMapper extends BaseMapper<PlainUserFavorite> {
    @Select("""
            select t1.video_group_id
            from
                (select video_group_id
                from plain_user_favorite
                where user_id = #{userId}
                order by id desc
                ) t1
                join video_group vg on vg.id = t1.video_group_id
            where type = #{type}
            limit #{skip}, #{pageSize}
    """)
    List<PlainUserFavorite> pageFavoriteWithType(Long userId, Byte type, Integer skip, Integer pageSize);

    @Select("""
            select count(1)
            from
                (select video_group_id
                from plain_user_favorite
                where user_id = #{userId}
                order by id desc
                ) t1
                join video_group vg on vg.id = t1.video_group_id
            where type = #{type}
    """)
    Integer countFavoriteWithType(Long userId, Byte type);
}
