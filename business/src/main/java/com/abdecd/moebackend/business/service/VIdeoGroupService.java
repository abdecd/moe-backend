package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.pojo.dto.commonVideoGroup.VIdeoGroupDTO;
import com.abdecd.moebackend.business.pojo.vo.common.VideoGroupVO;
import org.springframework.context.annotation.Bean;

public interface VIdeoGroupService {
    Long insert(VIdeoGroupDTO videoGroupDTO);

    void delete(Long id);

    void update(VIdeoGroupDTO videoGroupDTO);

    VideoGroupVO getById(Long id);
}
