package com.mfd.dto.permission;

import lombok.Data;

@Data
public class Permission {

    private Integer perId;
    private String perName;
    private String perType;
    private String perUrl;
    private String icon;
    private String permission;
    private String description;
    private Integer fid;

}
