package com.abdecd.moebackend.business.pojo.vo.common.commonVideoGroup;

import lombok.Data;

import java.util.ArrayList;

@Data
public class VideoGroupListVO {
    private Integer total;
    private ArrayList<VideoGroupVO> records;
}
