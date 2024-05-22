package com.abdecd.moebackend.business.pojo.dto.plainuser;

import com.abdecd.moebackend.business.dao.entity.PlainUserDetail;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdatePlainUserDTO {
    @Nullable
    @Pattern(regexp = "^[^ ]+$")
    String nickname;
    MultipartFile avatar;
    String signature;

    public PlainUserDetail toEntity(Long userId) {
        return new PlainUserDetail()
                .setUserId(userId)
                .setNickname(nickname)
                .setSignature(signature);
    }
}
