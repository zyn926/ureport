package com.mfd.ureport.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UreportFileEntity {

    private Integer repId;
    private String repName;
    private String repContent;
    private Date createTime;
    private Date upTime;

}
