package com.manji.base.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Getter
@Setter
public class UserDTO {
    private String id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private Integer gender;
    private LocalDate birthday;
    private String avatar;
    private String backgroundImg;
    private Integer status;
    private List<String> roles;
    private Map<String, List<String>> permissions;
}

