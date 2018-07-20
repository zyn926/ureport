package com.mfd.util;

import com.mfd.dto.auUserInfo.AuUserInfo;
import com.mfd.dto.log.LoOperationLog;
import com.mfd.service.log.OperationLogService;
import com.mfd.util.ServerResponse.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class OperationLogUtil {

    @Autowired
    private OperationLogService operationLogService;

    private static OperationLogUtil operationLogUtil;

    @PostConstruct
    private void init(){
        operationLogUtil = this;
    }

    public static void operationLog(ServerResponse response,String operationContent){
        if (response.isSuccess()) {
            Subject currentUser = SecurityUtils.getSubject();
            AuUserInfo userInfo = (AuUserInfo) currentUser.getPrincipal();
            /*LoOperationLog loOperationLog = new LoOperationLog().builder().id(KeyUtil.genUniqueKey()).username(userInfo.getUserName()).name(userInfo.getName()).operationContent(operationContent).build();*/
            LoOperationLog loOperationLog =new LoOperationLog(KeyUtil.genUniqueKey(),userInfo.getUserName(),userInfo.getName(),operationContent);
            if (!operationLogUtil.operationLogService.save(loOperationLog).isSuccess()){
                log.error("保存操作日志失败{}",loOperationLog.toString());
            }
        }
    }

    public static void operationLog(String operationContent){
        Subject currentUser = SecurityUtils.getSubject();
        AuUserInfo userInfo = (AuUserInfo) currentUser.getPrincipal();
        LoOperationLog loOperationLog =new LoOperationLog(KeyUtil.genUniqueKey(),userInfo.getUserName(),userInfo.getName(),operationContent);
        if (!operationLogUtil.operationLogService.save(loOperationLog).isSuccess()){
            log.error("保存操作日志失败{}",loOperationLog.toString());
        }
    }


}
