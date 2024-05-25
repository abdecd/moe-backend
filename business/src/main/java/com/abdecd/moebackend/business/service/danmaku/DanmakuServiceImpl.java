package com.abdecd.moebackend.business.service.danmaku;

import com.abdecd.moebackend.business.dao.entity.Danmaku;
import com.abdecd.moebackend.business.dao.mapper.DanmakuMapper;
import com.abdecd.moebackend.business.lib.CacheByFrequency;
import com.abdecd.moebackend.business.lib.CacheByFrequencyFactory;
import com.abdecd.moebackend.business.pojo.dto.danmaku.AddDanmakuDTO;
import com.abdecd.moebackend.business.pojo.vo.danmaku.DanmakuVO;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DanmakuServiceImpl implements DanmakuService {
    @Autowired
    private DanmakuMapper danmakuMapper;
    private CacheByFrequency<List<DanmakuVO>> danmakuCache;
    @Autowired
    public void setDanmakuCache(CacheByFrequencyFactory cacheByFrequencyFactory) {
        danmakuCache = cacheByFrequencyFactory.create(RedisConstant.DANMAKU, 200, 15);
    }

    @Override
    public Long addDanmaku(AddDanmakuDTO addDanmakuDTO) {
        // todo 检验弹幕池插入权限
        var entity = addDanmakuDTO.toEntity(UserContext.getUserId());
        danmakuMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public List<DanmakuVO> getDanmaku(Long videoId, Integer segmentIndex) {
        danmakuCache.recordFrequency(videoId + ":" + segmentIndex);
        return danmakuCache.get(videoId + ":" + segmentIndex, () -> {
            var list = danmakuMapper.selectList(new LambdaQueryWrapper<Danmaku>()
                    .eq(Danmaku::getVideoId, videoId)
                    .ge(Danmaku::getBegin, (segmentIndex - 1) * 360)
                    .lt(Danmaku::getBegin, segmentIndex * 360)
                    .orderByDesc(Danmaku::getTime)
                    .last("limit 5000")
            );
            return new ArrayList<>(list.stream().map(x -> {
                var danmakuVO = new DanmakuVO();
                BeanUtils.copyProperties(x, danmakuVO);
                return danmakuVO;
            }).toList());
        }, null, null);
    }

    @Override
    public void deleteDanmaku(Long id) {
        var danmaku = danmakuMapper.selectById(id);
        if (danmaku == null) return;
        if (
                danmaku.getUserId().equals(UserContext.getUserId())
                && danmaku.getTimestamp().isAfter(LocalDateTime.now().minusMinutes(2))
        ) {
            danmakuMapper.deleteById(id);
        }
    }

    @Cacheable(value = RedisConstant.VIDEO_DANMAKU_CNT, key = "#videoId")
    @Override
    public Long getDanmakuCount(Long videoId) {
        return danmakuMapper.selectCount(new LambdaQueryWrapper<Danmaku>()
                .eq(Danmaku::getVideoId, videoId)
        );
    }
}
