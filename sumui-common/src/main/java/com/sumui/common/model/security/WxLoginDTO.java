package com.sumui.common.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxLoginDTO implements Serializable {
    private String code;
    private String encryptedData;
    private String iv;
    private String rawData;
    private String signature;
    private String userInfo;
}
