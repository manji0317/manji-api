package com.manji.base.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private List<MenuDTO> menus;
}

