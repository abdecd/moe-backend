package com.abdecd.moebackend.business.service.backstage;

public interface VideoGroupAndTagService {
    void insert(Long tagId,Long groupId);

    void insertByTags(String tags, Long groupId);

    void deleteByVideoGroupId(Long id);
}
