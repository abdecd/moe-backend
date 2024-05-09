package com.abdecd.moebackend.business.service.impl;

import com.abdecd.moebackend.business.dao.entity.BangumiVideoGroup;
import com.abdecd.moebackend.business.dao.mapper.BangumiVideoGroupMapper;
import com.abdecd.moebackend.business.pojo.dto.BangumiVideoGroup.BangumiVideoGroupUpdateDTO;
import com.abdecd.moebackend.business.pojo.vo.common.BangumiVideoGroupVO;
import com.abdecd.moebackend.business.service.BangumiVideoGroupSever;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BangumiVideoGroupSeverlmpl implements BangumiVideoGroupSever {
    @Resource
    private BangumiVideoGroupMapper bangumiVideoGroupMapper;

    @Override
    public void deleteByVid(Long id) {
        bangumiVideoGroupMapper.deleteByVid(id);
    }

    @Override
    public void insert(BangumiVideoGroup bangumiVideoGroup) {
        bangumiVideoGroupMapper.insert(bangumiVideoGroup);
    }

    @Override
    public void update(BangumiVideoGroupUpdateDTO bangumiVideoGroupUpdateDTO) {
        BangumiVideoGroup bangumiVideoGroup = new BangumiVideoGroup();

        bangumiVideoGroup.setVideoGroupId(bangumiVideoGroupUpdateDTO.getId());
        if(bangumiVideoGroupUpdateDTO.getStatus() != null)
            bangumiVideoGroup.setStatus(Integer.valueOf(bangumiVideoGroupUpdateDTO.getStatus()));
        bangumiVideoGroup.setReleaseTime(bangumiVideoGroupUpdateDTO.getReleaseTime());
        bangumiVideoGroup.setUpdateAtAnnouncement(bangumiVideoGroupUpdateDTO.getUpdateAtAnnouncement());

        bangumiVideoGroupMapper.update(bangumiVideoGroup);
    }

    @Override
    public BangumiVideoGroupVO getByVid(BangumiVideoGroupVO bangumiVideoGroupVO) {
        BangumiVideoGroup bangumiVideoGroup = bangumiVideoGroupMapper.selectByVid(bangumiVideoGroupVO.getVideoGroupId());
        log.info("bangumiVideoGroup:{}", bangumiVideoGroup);

        bangumiVideoGroupVO.setReleaseTime(bangumiVideoGroup.getReleaseTime());
        bangumiVideoGroupVO.setUpdateAtAnnouncement(bangumiVideoGroup.getUpdateAtAnnouncement());
        bangumiVideoGroupVO.setStatus(bangumiVideoGroup.getStatus());

        return bangumiVideoGroupVO;
    }
}
