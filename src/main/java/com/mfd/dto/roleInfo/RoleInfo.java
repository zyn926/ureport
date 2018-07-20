package com.mfd.dto.roleInfo;

import com.mfd.dto.permission.Permission;
import lombok.Data;

import java.util.List;

@Data
public class RoleInfo {

    private Integer roleId;
    private String roleName;
    private String description;
    private String perIds;
    private List<Permission> permissons;
}
