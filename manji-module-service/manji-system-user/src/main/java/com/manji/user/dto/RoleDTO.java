package com.manji.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 接受前台对Role操作的实体类
 */
@Getter
@Setter
public class RoleDTO {
    private String id;
    private String roleName;
    private String description;
    private List<String> menus;
}
