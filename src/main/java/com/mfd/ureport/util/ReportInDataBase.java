package com.mfd.ureport.util;


import com.bstek.ureport.provider.report.ReportFile;
import com.bstek.ureport.provider.report.ReportProvider;
import com.mfd.ureport.dao.UreportFileMapper;
import com.mfd.ureport.dto.UreportFileEntity;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Component
// 该注解可以利用其 prefix属性值 + 类的属性名 在yml中配置属性值
//@ConfigurationProperties(prefix = "ureport.postgresql.provider")
public class ReportInDataBase implements ReportProvider{
    private static final String NAME = "数据库文件系统";

    // 特定前缀，ureport底层会调用 getPrefix 方法来获取报表操作的Provier类
    private String prefix = "database:";

    // 是否禁用
    private boolean disabled = false;

    @Autowired
    private UreportFileMapper ureportFileMapper;

    @Override
    public InputStream loadReport(String file) {
        UreportFileEntity ureportFileEntity = ureportFileMapper.queryUreportFileEntityByName(getCorrectName(file));
        byte[] content = null;
        try {
            content = ureportFileEntity.getRepContent().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        return inputStream;
    }

    @Override
    public void deleteReport(String file) {
        ureportFileMapper.deleteReportFileByName(getCorrectName(file));
    }

    @Override
    public List<ReportFile> getReportFiles() {
        List<UreportFileEntity> list = ureportFileMapper.queryReportFileList();
        List<ReportFile> reportList = new ArrayList<>();
        for (UreportFileEntity ureportFileEntity : list) {
            reportList.add(new ReportFile(ureportFileEntity.getRepName(), ureportFileEntity.getUpTime()));
        }
        return reportList;
    }

    @Override
    public void saveReport(String file, String content) {
        file = getCorrectName(file);
        UreportFileEntity ureportFileEntity = ureportFileMapper.queryUreportFileEntityByName(file);
        Date currentDate = new Date();
        if(ureportFileEntity == null){
            ureportFileEntity = new UreportFileEntity();
            ureportFileEntity.setRepName(file);
            ureportFileEntity.setRepContent(content);
            ureportFileEntity.setCreateTime(currentDate);
            ureportFileEntity.setUpTime(currentDate);
            ureportFileMapper.insertReportFile(ureportFileEntity);
        }else{
            ureportFileEntity.setRepContent(content);
            ureportFileMapper.updateReportFile(ureportFileEntity);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean disabled() {
        return disabled;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    /**
     * 获取没有前缀的文件名
     * @param name
     * @return
     */
    private String getCorrectName(String name){
        if(name.startsWith(prefix)){
            name = name.substring(prefix.length(), name.length());
        }
        return name;
    }

}
