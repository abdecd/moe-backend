package com.abdecd.moebackend.business.lib;

import cn.hutool.core.util.URLUtil;
import com.abdecd.moebackend.business.common.property.MoeProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class BiliParser {
    @Autowired
    private OkHttpClient okHttpClient;
    @Autowired
    private MoeProperties moeProperties;
    @Autowired
    private ObjectMapper objectMapper;

    private final Map<String, String> bvQualityMap = Map.of("360P","16", "480P","32", "720P","64", "1080P","80");

    public String parseBV(String bvid, String quality, String p) throws IOException {
        try (var resp = okHttpClient.newCall(new Request.Builder()
                .url(HttpUrl.get(moeProperties.getBvUrl()).newBuilder()
                        .addQueryParameter("bvid", bvid)
                        .addQueryParameter("SESSDATA", moeProperties.getBiliSession())
                        .addQueryParameter("qn", bvQualityMap.get(quality))
                        .addQueryParameter("p", p)
                        .build()
                )
                .build()
        ).execute()) {
            if (resp.code() != 200) throw new IOException();
            if (resp.body() != null) {
                var json = objectMapper.readTree(resp.body().string());
                System.out.println(json.toPrettyString());
                var url = json.get("data")
                        .get("durl")
                        .get(0)
                        .get("url");
                return moeProperties.getProxyPrefix() + URLUtil.encodeAll(url.textValue());
            }
        }
        return null;
    }

}
