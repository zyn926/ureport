package com.mfd.controller.roleController;

import com.mfd.dto.roleInfo.RoleInfo;
import com.mfd.service.roleService.RoleService;
import com.mfd.util.OperationLogUtil;
import com.mfd.util.ServerResponse.ServerResponse;
import com.mfd.util.paging.PageQuery;
import com.mfd.util.paging.PageResult;
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

@Controller
@Slf4j
@RequestMapping(value = "role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/page")
    public String page(){
        return "role/roleList";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public ServerResponse page(PageQuery page){
        PageResult<RoleInfo> result = roleService.getPageList(page);
        return ServerResponse.createBySuccess(result);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse save(RoleInfo role){
        ServerResponse response = roleService.save(role);
        OperationLogUtil.operationLog(response,"新增角色：" + role.getRoleName());
        return response;
    }

    /**
     * 修改角色权限
     * @param roleId
     * @param perIds
     * @param roleName
     * @return
     */
    @RequestMapping(value = "/updateRolePer",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateRolePer(@PathParam("roleId") Integer roleId, @PathParam("perIds") String perIds, @PathParam("roleName")String roleName){
        ServerResponse response =  roleService.updateRolePer(roleId,perIds);
        OperationLogUtil.operationLog(response,"修改ID为："+roleId+" 名称为：" + roleName+"  角色的权限");
        return response;
    }

    @RequestMapping(value = "/deleteById",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse deleteById(@PathParam("roleId") Integer roleId, @PathParam("roleName") String roleName){
        ServerResponse response = roleService.deleteById(roleId);
        OperationLogUtil.operationLog(response,"删除ID为："+roleId+" 名称为：" + roleName+" 的角色");

        return response;
    }

    /**
     * 用于拼装权限树
     * @return
     */
    @RequestMapping(value = "/getPerTree")
    @ResponseBody
    public List<HashMap<String,Object>> getPerTree(){
        return roleService.getTree();
    }

    /**
     * 查询角色对应的权限
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/selectPerIdsByRole",method = RequestMethod.GET)
    @ResponseBody
    public String selectPerIdsByRole(Integer roleId){
        return roleService.selectPerIdsByRole(roleId);
    }


}
