package com.sumui.common.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookFamilyUsersDTO implements Serializable {
    private String userId;
    private String username;
    private String nickname;
}
