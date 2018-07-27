package com.mfd.dto.auUserInfo;

import com.mfd.dto.roleInfo.RoleInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AuUserInfo implements Serializable{

    private Integer userId;
    private String userName;
    private String name;
    private String password;
    private Date createTime;
    private Date upTime;
    private String roleIds;
    private String roleNames;
    private List<RoleInfo> roleList;
}
