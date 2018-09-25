package com.mfd.ureport.util;

import com.bstek.ureport.definition.datasource.BuildinDatasource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@EnableAutoConfiguration
@Configuration
//@ConfigurationProperties(prefix = "datasource")
@Slf4j
public class UreportDataSource implements BuildinDatasource{

    private static final String NAME = "系统数据源";

    @Resource
    private DataSource dataSource;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error("Ureport 数据源 获取连接失败！");
            e.printStackTrace();
        }
        return null;
    }

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }
}
