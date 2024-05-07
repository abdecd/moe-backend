package com.abdecd.moebackend.business.pojo.dto.danmaku;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import com.abdecd.moebackend.business.common.util.SensitiveUtils;
import com.abdecd.moebackend.business.dao.entity.Danmaku;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.zip.CRC32;

@Data
public class AddDanmakuDTO {
    @NotNull
    Long videoId;
    @NotNull
    Double begin;
    @NotNull
    Integer mode;
    @NotNull
    Integer size;
    @NotBlank
    String color;
    @NotNull
    Long time;
    @NotNull
    Integer pool;
    @NotBlank
    String text;

    public Danmaku toEntity(Long userId) {
        var danmaku = new Danmaku();
        BeanUtils.copyProperties(this, danmaku);

        var crc32 = new CRC32();
        crc32.update(("user" + userId + "salt" + videoId).getBytes(StandardCharsets.UTF_8));
        var crc32result = ByteArrayUtil.toHexString(
                Arrays.copyOfRange(ByteBuffer.allocate(Long.SIZE / Byte.SIZE)
                        .putLong(crc32.getValue())
                        .array(), 4, 8)
        );

        danmaku.setUserId(userId)
                .setTimestamp(LocalDateTime.now())
                .setAuthor(crc32result)
                .setText(SensitiveUtils.sensitiveFilter(text));

        return danmaku;
    }
}
