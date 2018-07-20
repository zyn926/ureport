package com.mfd.service.roleService;

import com.mfd.dao.roleMapper.RoleMapper;
import com.mfd.dto.roleInfo.RoleInfo;
import com.mfd.util.ServerResponse.ServerResponse;
import com.mfd.util.paging.PageQuery;
import com.mfd.util.paging.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public PageResult<RoleInfo> getPageList(PageQuery page){
        int count = roleMapper.count();
        if(count >0 ){
            List<RoleInfo> list = roleMapper.getPageList(page);
            return PageResult.<RoleInfo>builder().total(count).data(list).build();
        }
        return PageResult.<RoleInfo>builder().build();
    }

    @Transactional
    public ServerResponse deleteById(Integer roleId){
        int roleFlag = roleMapper.delete(roleId);
        int roleUserFlag = roleMapper.deleteUserRole(roleId);
        int rolePerFlag = roleMapper.deleteRolePer(roleId);
        if(roleFlag >= 0 && roleUserFlag >= 0 && rolePerFlag >= 0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    @Transactional
    public ServerResponse save(RoleInfo role){
        if(roleMapper.selectByName(role.getRoleName()) > 0){
            return ServerResponse.createByErrorMessage("此角色已经存在！");
        }else{
            int roleFlag = roleMapper.insert(role);
            String perIdStr = role.getPerIds();
            boolean perFlag = false;
            if(perIdStr != null && !("").equals(perIdStr)){
                Integer roleId = role.getRoleId();
                String[] perIds = perIdStr.split(",");
                List<Map> perList = new ArrayList<>();
                for (String perId : perIds) {
                    Map<String,Object> perMap = new HashMap<>();
                    perMap.put("roleId",roleId);
                    perMap.put("perId",Integer.parseInt(perId));
                    perList.add(perMap);
                }
                if(roleMapper.insertRolePer(perList) > 0){
                    perFlag = true;
                }else{
                    perFlag = false;
                }
            }else{
                perFlag = true;
            }
            if(roleFlag > 0 && perFlag){
                return ServerResponse.createBySuccess();
            }else{
                return ServerResponse.createByError();
            }
        }
    }

    public List<HashMap<String,Object>> getTree(){
        return roleMapper.selectAllPer();
    }

    public String selectPerIdsByRole(Integer roleId){
        return roleMapper.selectPerIdsByRole(roleId);
    }

    public ServerResponse updateRolePer(Integer roleId,String perIdStr){
        roleMapper.deleteRolePer(roleId);
        String[] perIds = perIdStr.length() > 0 ? perIdStr.split(",") : null;
        if(perIds != null ){
            List<Map> perIdsList = new ArrayList<>();
            for (String perId : perIds) {
                Map<String,Object> map = new HashMap<>();
                map.put("roleId",roleId);
                map.put("perId",Integer.parseInt(perId));
                perIdsList.add(map);
            }
            if(roleMapper.insertRolePer(perIdsList) > 0 ){
                return ServerResponse.createBySuccess();
            }else{
                return  ServerResponse.createByError();
            }
        }else{
            return ServerResponse.createBySuccess();
        }
    }
}
