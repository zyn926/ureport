package com.mfd.dao.roleMapper;

import com.mfd.dto.roleInfo.RoleInfo;
import com.mfd.util.paging.PageQuery;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface RoleMapper {

    @Select("select role_id,role_name,description from au_role order by role_name desc limit #{page.offset}, #{page.pageSize}")
    List<RoleInfo> getPageList(@Param("page")PageQuery page);

    @Select("select count(role_id) from au_role")
    int count();

    @Delete("delete from au_role where role_id = #{roleId}")
    int delete(@Param("roleId") Integer roleId);

    @Options(useGeneratedKeys=true, keyProperty="role_id")
    /*@SelectKey(statement="select last_value from au_role_role_id_seq",keyProperty = "roleId",before=false, resultType=Integer.class)*/
    @Insert("insert into au_role(role_name,description) values (#{roleName},#{description})")
    int insert(RoleInfo role);

    /**
     * 查询是否存在重复的角色名
     * @param roleName
     * @return
     */
    @Select(" select count(role_id) from au_role where role_name = #{roleName}")
    int selectByName(@Param("roleName") String roleName);

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @Delete("delete from au_user_role where role_id = #{roleId}")
    int deleteUserRole(@Param("roleId") Integer roleId);

    /**
     * 查询所有的权限用于生成权限树
     * @return
     */
    @Select("select per_id as \"id\" ,per_name as \"name\",fid as \"pId\" from au_permission ")
    List<HashMap<String,Object>> selectAllPer();

    /**
     * 插入角色和权限的关系
     * @param perIds
     * @return
     */
    @Insert("<script> insert into au_role_permission (role_id,per_id) values <foreach item = \"item\" index = \"index\" collection = \"perIds\" separator = \",\"> (#{item.roleId},#{item.perId})</foreach> </script>")
    int insertRolePer(@Param("perIds")List<Map> perIds);

    /**
     * 根据角色id查询已有的权限
     * @param roleId
     * @return
     */
    @Select("select group_concat(per_id) from au_role_permission where role_id = #{roleId}")
    String selectPerIdsByRole(@Param("roleId") Integer roleId);

    /**
     * 删除角色权限表对应的信息
     * @param roleId
     * @return
     */
    @Delete("delete from au_role_permission where role_id = #{roleId}")
    int deleteRolePer(@Param("roleId") Integer roleId);
}
