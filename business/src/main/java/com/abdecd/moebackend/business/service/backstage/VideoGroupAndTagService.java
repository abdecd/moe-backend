package com.abdecd.moebackend.business.service.backstage;

import com.abdecd.moebackend.business.pojo.dto.backstage.bangumiVideoGroup.BangumiVideoGroupUpdateDTO;

public interface VideoGroupAndTagService {
    void insert(Long tagId,Long groupId);

    void insertByTags(String tags, Long groupId);

    void deleteByVideoGroupId(Long id);

    void update(BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO);
}
