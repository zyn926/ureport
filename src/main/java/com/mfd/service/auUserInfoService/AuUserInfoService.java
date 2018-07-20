package com.mfd.service.auUserInfoService;

import com.mfd.dao.auUserInfo.AuUserInfoMapper;
import com.mfd.dto.auUserInfo.AuUserInfo;
import com.mfd.dto.roleInfo.RoleInfo;
import com.mfd.util.Md5Util;
import com.mfd.util.ServerResponse.ServerResponse;
import com.mfd.util.paging.PageQuery;
import com.mfd.util.paging.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuUserInfoService {

    @Autowired
    private AuUserInfoMapper auUserMapper;

    public AuUserInfo findByUsername(String usrName){
        AuUserInfo userInfo=auUserMapper.getUserByName(usrName);
        /*userInfo.setRoleList(auUserMapper.getRolesByUseId(userInfo.getUserId()));
        for(RoleInfo r:userInfo.getRoleList()){
            r.setPermissons(auUserMapper.getPermissonByRoleId(r.getRoleId()));
        }*/
        return userInfo;
    }

    public List<Map<Object,Object>> getMenu(Integer userId){
        return auUserMapper.getMenu(userId);
    }


    public PageResult<AuUserInfo> getPageList(PageQuery page) {

        int count = auUserMapper.count(page.getParam());
        if (count > 0) {
            List<AuUserInfo> list = auUserMapper.getPageList(page);
            return PageResult.<AuUserInfo>builder().total(count).data(list).build();
        }
        return PageResult.<AuUserInfo>builder().build();
    }

    @Transactional
    public ServerResponse save(AuUserInfo user) {
        if(auUserMapper.selectByName(user.getUserName()) >  0){
            return ServerResponse.createByErrorMessage("此用户已经存在");
        }else{
            String password = user.getPassword();
            password = Md5Util.getMd5(password);
            user.setPassword(password);
            int userFlag = auUserMapper.insert(user);
            String roleIdStr = user.getRoleIds();
            boolean roleFlag = false;
            if (roleIdStr != null) {
                String[] roleIds = roleIdStr.split(",");
                ArrayList<HashMap> roleList = new ArrayList<>();
                Integer userId = user.getUserId();
                for (String roleId : roleIds) {
                    HashMap<String, Object> roleMap = new HashMap<>();
                    roleMap.put("userId", userId);
                    roleMap.put("roleId", roleId);
                    roleList.add(roleMap);
                }
                if (auUserMapper.insertUserRole(roleList) > 0) {
                    roleFlag = true;
                }
            } else {
                roleFlag = true;
            }
            if (userFlag > 0 && roleFlag) {
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createByError();
            }
        }
    }

    public List<RoleInfo> selectAllRole() {
        return auUserMapper.selectAllRole();
    }

    public Map selectRoleByUser(String userId) {
        return auUserMapper.selectRoleByUser(userId);
    }

    public ServerResponse updateUserRole(String userId, String roleIdStr) {
        auUserMapper.deleteUserRole(userId);
        String[] roleIds = roleIdStr.length() > 0 ? roleIdStr.split(",") : null;
        if (roleIds != null) {
            ArrayList<HashMap> roleList = new ArrayList<>();
            for (String roleId : roleIds) {
                HashMap<String, Object> roleMap = new HashMap<>();
                roleMap.put("userId", userId);
                roleMap.put("roleId", roleId);
                roleList.add(roleMap);
            }
            if (auUserMapper.insertUserRole(roleList) > 0) {
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createByErrorMessage("用户角色设置失败");
            }
        } else {
            return ServerResponse.createBySuccess();
        }
    }

    @Transactional
    public ServerResponse deleteById(Integer userIdStr) {
        if (userIdStr == 1) {
            return ServerResponse.createByErrorMessage("超级管理员不能删除！");
        } else {
            int userFlag = auUserMapper.deleteById(userIdStr);
            int userRoleFlag = auUserMapper.deleteUserAndRole(userIdStr);
            if (userFlag >= 0 && userRoleFlag >= 0) {
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createByError();
            }
        }
    }

    public boolean checkPass(Integer userId,String pass){
        pass = new Md5Util().getMd5(pass);
        boolean flag = false;
        int count = auUserMapper.checkPass(userId,pass);
        if(count > 0){
            flag = true;
        }
        return flag;
    }

    public boolean updatePass(Integer userId,String pass){
        pass = new Md5Util().getMd5(pass);
        boolean flag = false;
        int count = auUserMapper.updatePass(userId,pass);
        if(count > 0){
            flag = true;
        }
        return flag;
    }
}
