package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.pojo.dto.commonVideoGroup.VIdeoGroupDTO;
import com.abdecd.moebackend.business.pojo.vo.common.VideoGroupVO;

public interface VIdeoGroupService {
    Long insert(VIdeoGroupDTO videoGroupDTO);

    void delete(Long id);

    VideoGroupVO update(VIdeoGroupDTO videoGroupDTO);

    VideoGroupVO getById(Long id);
}
