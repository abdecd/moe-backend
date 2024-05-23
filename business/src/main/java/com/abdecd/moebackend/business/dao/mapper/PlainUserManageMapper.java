package com.abdecd.moebackend.business.dao.mapper;

import com.abdecd.moebackend.business.dao.entity.UserManage;
import com.abdecd.moebackend.business.pojo.vo.plainuser.AllVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface PlainUserManageMapper extends BaseMapper<UserManage> {

    @Select("<script>" +
            "SELECT id, user.nickname, email, status, permission, create_time, avatar, signature " +
            "FROM user LEFT JOIN plain_user_detail ON user.id = plain_user_detail.user_id " +
            "<where>" +
            "<if test='id != null'>AND id = #{id}</if> " +
            "<if test='name != null'>AND user.nickname LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='status != null'>AND status = #{status}</if> " +
            "</where> " +
            "</script>")
    Page<AllVO> selectUsers(Page<?> page, @Param("id") Long id, @Param("name") String name, @Param("status") Integer status);
}
