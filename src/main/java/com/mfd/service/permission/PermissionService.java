package com.mfd.service.permission;

import com.mfd.dao.permission.PermissionMapper;
import com.mfd.dto.permission.Permission;
import com.mfd.util.ServerResponse.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public List<Permission> getPerList(){
        return permissionMapper.getPerList();
    }

    public List<HashMap> getTree(){
        return permissionMapper.selectAllPer();
    }

    public ServerResponse save(Permission permission){
        Integer fid = permission.getFid();
        String icon = permission.getIcon();
        if(fid == 0 && (icon.equals("") || icon.equals("null"))){
            permission.setIcon("fa fa-folder");
        }
        if(permissionMapper.insert(permission) > 0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    public ServerResponse update(Permission permission){
        Integer fid = permission.getFid();
        String icon = permission.getIcon();
        if(fid == 0 && (icon.equals("") || icon.equals("null"))){
            permission.setIcon("fa fa-folder");
        }
        if(permissionMapper.update(permission) > 0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    public ServerResponse deleteById(Integer perId){

        List<Integer> perIdList = getChildId(perId,new ArrayList<>());
        String perName = getPerName(perIdList);
        int rolePerFlsg = permissionMapper.deleteRolePer(perId);
        int perFlag = permissionMapper.deleteById(perIdList);
        if(perFlag >= 0 && rolePerFlsg >= 0){
            return ServerResponse.createBySuccessMessage(perName);
        }else{
            return ServerResponse.createByError();
        }
    }

    public Map selectById(Integer perId){
        return permissionMapper.selectById(perId);
    }

    public ServerResponse selectChildById(Integer pId){
        if(permissionMapper.selectChildById(pId) > 0){
            return ServerResponse.createByError();
        }else{
            return ServerResponse.createBySuccess();
        }
    }

    public List<Integer> getChildId(Integer pId,List<Integer> list) {
        list.add(pId);
        List<Integer> listItem = permissionMapper.selectChildId(pId);
        if (listItem.size() > 0) {
            for (int i = 0, l = listItem.size(); i < l; i++) {
                list = getChildId(listItem.get(i), list);
            }
        }
        return list;
    }

    public String getPerName(List<Integer> idList){
        return permissionMapper.selectPerName(idList);
    }

    public List<Map<String,String>> getReport(){
        return permissionMapper.getReport();
    }

}
