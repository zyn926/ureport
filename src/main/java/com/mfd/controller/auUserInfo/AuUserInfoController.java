package com.mfd.controller.auUserInfo;

import com.mfd.dto.auUserInfo.AuUserInfo;
import com.mfd.dto.roleInfo.RoleInfo;
import com.mfd.service.auUserInfoService.AuUserInfoService;
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
import java.util.Map;

@Controller
@Slf4j
@RequestMapping(value = "/auUser")
public class AuUserInfoController {

    @Autowired
    private AuUserInfoService auUserInfoService;

    @RequestMapping(value = "/page")
    public String page(){return "auUser/auUserInfoList";}

    @RequestMapping(value = "/list")
    @ResponseBody
    public ServerResponse page(PageQuery page){
        PageResult<AuUserInfo> result = auUserInfoService.getPageList(page);
        return ServerResponse.createBySuccess(result);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse save(AuUserInfo user) {
        ServerResponse response = auUserInfoService.save(user);
        OperationLogUtil.operationLog(response,"新增用户："+user.getUserName());
        return response;
    }

    /**
     * 查询全部的角色
     * @return
     */
    @RequestMapping(value = "/selectAllRole",method = RequestMethod.GET)
    @ResponseBody
    public List<RoleInfo> selectAllRole(){
        return auUserInfoService.selectAllRole();
    }

    /**
     * 查询用户的角色
     * @param userId
     * @return
     */
    @RequestMapping(value = "/selectRoleByUser",method = RequestMethod.GET)
    @ResponseBody
    public Map selectRoleByUser(String userId){
        return auUserInfoService.selectRoleByUser(userId);
    }

    /**
     * 修改用户的角色
     * @param userId
     * @param roleId
     * @param userName
     * @return
     */
    @RequestMapping(value = "/updateUserRole",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateUserRole(@PathParam("userId") String userId,
                                         @PathParam("roleId") String roleId,
                                         @PathParam("userName") String userName){

        ServerResponse response = auUserInfoService.updateUserRole(userId,roleId);
        OperationLogUtil.operationLog(response,"更改用户名为： "+userName+" 的用户角色");

        return response;
    }

    @RequestMapping(value = "/deleteById",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse deleteById(@PathParam("userIdStr") Integer userIdStr, @PathParam("userName") String userName){
        ServerResponse response = auUserInfoService.deleteById(userIdStr);
        OperationLogUtil.operationLog(response,"删除ID为："+userIdStr+" 用户名："+userName+" 的用户");
        return response;
    }

    /**
     * 验证密码
     * @param oldPass
     * @return
     */
    @RequestMapping(value = "/checkPass",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> checkPass(String oldPass){
        Subject currentUser = SecurityUtils.getSubject();
        AuUserInfo userInfo = (AuUserInfo) currentUser.getPrincipal();
        Integer userId = userInfo.getUserId();
        Map<String,Object> map = new HashMap<>();
        if(auUserInfoService.checkPass(userId,oldPass)){
            map.put("valid",true);
        }else{
            map.put("valid",false);
        }
        return map;
    }

    /**
     * 修改密码
     * @param pass
     * @return
     */
    @RequestMapping(value = "/changePass",method = RequestMethod.POST)
    @ResponseBody
    public boolean changePass(@PathParam("pass") String pass){
        Subject currentUser = SecurityUtils.getSubject();
        AuUserInfo userInfo = (AuUserInfo) currentUser.getPrincipal();
        Integer userId = userInfo.getUserId();
        boolean flag = auUserInfoService.updatePass(userId,pass);
        if(flag){
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                subject.logout();
            }
        }
        return flag;
    }

}
