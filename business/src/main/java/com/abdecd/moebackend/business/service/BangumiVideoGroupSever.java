package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.common.BangumiVideoGroupVO;

public interface BangumiVideoGroupSever {
    void deleteByVid(Long id);

    void insert(BangumiVideoGroup bangumiVideoGroup);

    void update(BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO);

    BangumiVideoGroupVO getByVid(BangumiVideoGroupVO bangumiVideoGroupVO);
}
