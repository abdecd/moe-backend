package com.abdecd.moebackend.business.pojo.dto.plainuser;

import com.abdecd.moebackend.business.dao.entity.PlainUserDetail;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdatePlainUserDTO {
    String nickname;
    MultipartFile avatar;
    String signature;

    public PlainUserDetail toEntity() {
        return new PlainUserDetail()
                .setNickname(nickname)
                .setSignature(signature);
    }
}
