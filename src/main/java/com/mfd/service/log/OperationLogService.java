package com.mfd.service.log;

import com.mfd.dao.log.LoOperationLogMapper;
import com.mfd.dto.log.LoOperationLog;
import com.mfd.util.ServerResponse.ServerResponse;
import com.mfd.util.paging.PageQuery;
import com.mfd.util.paging.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogService {
    @Autowired
    private LoOperationLogMapper loOperationLogMapper;

    public ServerResponse save(LoOperationLog loOperationLog){
        if (loOperationLogMapper.insert(loOperationLog) > 0){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("保存操作日志失败！");
    }

    public PageResult<LoOperationLog> list(PageQuery page){
        int count = loOperationLogMapper.count();
        if (count > 0){
            List<LoOperationLog> list = loOperationLogMapper.getPageList(page);
            return PageResult.<LoOperationLog>builder().total(count).data(list).build();
        }
        return PageResult.<LoOperationLog>builder().build();
    }
}
