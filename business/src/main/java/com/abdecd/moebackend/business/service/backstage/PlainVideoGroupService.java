package com.abdecd.moebackend.business.service.backstage;

import com.abdecd.moebackend.business.pojo.vo.videogroup.PlainVideoGroupVO;

import java.util.ArrayList;

public interface PlainVideoGroupService {
    ArrayList<PlainVideoGroupVO> getAllVideoGroup(Integer pageIndex, Integer pageSize, String id, String title, Byte status);

    Integer countPlainVideoGroup(String id, String title, Byte status);
}

