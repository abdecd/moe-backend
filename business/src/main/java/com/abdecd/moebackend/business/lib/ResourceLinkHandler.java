package com.abdecd.moebackend.business.lib;

import com.abdecd.moebackend.business.common.property.MoeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceLinkHandler {
    @Autowired
    MoeProperties moeProperties;

    public String getVideoLink(String path) {
        return moeProperties.getVideoBasePath() + "/" + path;
    }

    /**
     * 不以 "/" 开头 错误返回空字符串
     */
    public String getRawPathFromVideoLink(String link) {
        if (!link.startsWith(moeProperties.getVideoBasePath())) return "";
        return link.substring(moeProperties.getVideoBasePath().length() + 1);
    }
}
