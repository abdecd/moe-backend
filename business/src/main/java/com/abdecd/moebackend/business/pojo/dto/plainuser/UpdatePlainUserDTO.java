package com.abdecd.moebackend.business.pojo.dto.plainuser;

import com.abdecd.moebackend.business.dao.entity.PlainUserDetail;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdatePlainUserDTO {
    @Nullable
    @NotBlank
    String nickname;
    MultipartFile avatar;
    @Nullable
    @NotBlank
    String signature;

    public PlainUserDetail toEntity() {
        return new PlainUserDetail()
                .setNickname(nickname)
                .setSignature(signature);
    }
}
