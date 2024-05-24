package com.abdecd.moebackend.business.pojo.dto.video;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class AddVideoFullDTO extends AddVideoDTO {
    @NotNull
    Byte becomeVideoStatus;
}
