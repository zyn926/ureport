package com.mfd.controller.permission;

import com.mfd.dto.permission.Permission;
import com.mfd.service.permission.PermissionService;
import com.mfd.util.OperationLogUtil;
import com.mfd.util.ServerResponse.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping(value = "permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "/page")
    public String page(){
        return "permission/permissionList";
    }

    @RequestMapping(value = "/getPerList")
    @ResponseBody
    public List<Permission> getPerList(){
        return permissionService.getPerList();
    }

    /**
     * 生成权限树
     * @return
     */
    @RequestMapping(value = "/getPerTree")
    @ResponseBody
    public List<HashMap> getTree(){
        return permissionService.getTree();
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse save(Permission permission){
        ServerResponse response = permissionService.save(permission);
        OperationLogUtil.operationLog(response,"新增权限：" + permission.getPerName());
        return response;
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(Permission permission){
        ServerResponse response = permissionService.update(permission);
        OperationLogUtil.operationLog(response,"修改权限：" + permission.getPerName());
        return response;
    }

    @RequestMapping(value = "/selectById")
    @ResponseBody
    public Map selectById(Integer perId){
        return permissionService.selectById(perId);
    }

    @RequestMapping(value = "/deleteById",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse deleteById(@PathParam("perId") Integer perId){
        ServerResponse response = permissionService.deleteById(perId);
        String perNme = response.getMsg();
        if (response.isSuccess()) {
            if(perNme.indexOf(",") > 0){
                OperationLogUtil.operationLog(response,"删除 ID ："+perId+"，名称："+perNme.substring(0,perNme.indexOf(","))+" 的权限以及名称为： "+perNme.substring(perNme.indexOf(","))+" 的子权限");
            }else{
                OperationLogUtil.operationLog(response,"删除 ID ："+perId+"，名称："+perNme+" 的权限");
            }
        }
        return response;
    }

    @RequestMapping(value = "/selectChildById")
    @ResponseBody
    public ServerResponse selectChildById(Integer perId){
        return permissionService.selectChildById(perId);
    }

    @RequestMapping(value = "/getReport")
    @ResponseBody
    public List<Map<String,String>> getReport(){
        return permissionService.getReport();
    }
}
