package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.dao.entity.Video;

import java.util.ArrayList;

public interface VideoService {
    ArrayList<Video> getVideoListByGid(Integer videoGroupId);
}
