package com.mfd.ureport.service;

import com.mfd.ureport.dao.UreportFileMapper;
import com.mfd.ureport.dto.UreportFileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UreportService {

    @Autowired
    private UreportFileMapper ureportFileMapper;

    public List<UreportFileEntity> queryReportFileList(){
        return ureportFileMapper.queryReportFileList();
    }
}
