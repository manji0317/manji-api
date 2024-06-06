package com.manji.base.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 接受前台对Role操作的实体类
 */
@Getter
@Setter
public class RoleDTO {
    private String id;
    private String roleName;
    private String description;
    private Map<String, List<String>> permissions;
}
