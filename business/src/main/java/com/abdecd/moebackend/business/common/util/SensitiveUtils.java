package com.abdecd.moebackend.business.common.util;

import cn.hutool.dfa.SensitiveUtil;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SensitiveUtils {
    static {
        // 初始化敏感词
        var classPathResource = new ClassPathResource("sensitive/comment.txt");
        try (var reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()))) {
            var sensitiveWords = reader.lines().toList();
            SensitiveUtil.init(sensitiveWords);
        } catch (IOException e) {
            System.out.println("敏感词文件不存在");
        }
    }

    /**
     * 处理过滤文本中的敏感词，默认替换成*
     */
    public static String sensitiveFilter(String text) {
        return SensitiveUtil.sensitiveFilter(text);
    }
}
