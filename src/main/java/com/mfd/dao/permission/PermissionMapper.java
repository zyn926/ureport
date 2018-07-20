package com.mfd.dao.permission;

import com.mfd.dto.permission.Permission;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface PermissionMapper {

    @Select("select per_id,per_name,per_url,fid,description,icon from au_permission order by per_name ")
    List<Permission> getPerList();

    @Insert("insert into au_permission(per_name,per_url,fid,description,icon,per_type) values (#{perName},#{perUrl},#{fid},#{description},#{icon},#{perType})")
    int insert(Permission permission);

    @Update("<script> update au_permission <set> <if test=\" perName != null\">per_name = #{perName}</if> <if test=\" perUrl != null\">,per_url = #{perUrl}</if> <if test=\" fid != null\">,fid = #{fid}</if> <if test=\" description != null\">,description = #{description}</if> <if test=\"icon != null and icon != ''\"> ,icon = #{icon}</if> <if test=\"perType != '' and perType != null\">,per_type = #{perType}</if> </set> where per_id = #{perId} </script>")
    int update(Permission permission);

    /**
     * 删除权限以及子节点
     * @param idList
     * @return
     */
    @Delete("<script> delete from au_permission where per_id in <foreach item = \"item\" index = \"index\" collection = \"idList\" open = \"(\" separator = \",\" close = \")\" >#{item}</foreach> </script>")
    int deleteById(@Param("idList") List<Integer> idList);

    /**
     * 删除角色权限表对应的信息
     * @param perId
     * @return
     */
    @Delete("delete from au_role_permission where per_id = #{perId}")
    int deleteRolePer(@Param("perId") Integer perId);

    /**
     * 查询所有的权限用于生成权限树
     * @return
     */
    @Select("select per_id as id ,per_name as name,fid as \"pId\" from au_permission order by per_name desc")
    List<HashMap> selectAllPer();

    /**
     * 查询有没有子节点
     * @param pId
     * @return
     */
    @Select("select count(per_id) from au_permission where fid = #{pId}")
    int selectChildById(@Param("pId") Integer pId);

    /**
     * 修改查询单个信息
     * @param perId
     * @return
     */
    @Select("select t.per_id as \"perId\",t.per_name as \"perName\",t.per_url as \"perUrl\",t.fid,t.description,t.icon,ap.per_name as \"parentName\",t.per_type as \"perType\" from au_permission t left join au_permission ap on t.fid=ap.per_id where t.per_id =  #{perId}")
    Map selectById(Integer perId);

    /**
     * 用于递归查询子节点的id
     * @param pId
     * @return
     */
    @Select("select per_id from au_permission where fid = #{pId}")
    List<Integer> selectChildId(Integer pId);

    /**
     * 将删除的子节点名称拼接成字符串
     * @param idList
     * @return
     */
    @Select("<script> select string_agg(per_name) from au_permission where per_id in <foreach item = \"item\" index = \"index\" collection = \"idList\" open = \"(\" separator = \",\" close = \")\" >#{item}</foreach> </script>")
    String selectPerName(@Param("idList") List<Integer> idList);

    @Select(" select substring(rep_name from 1 for position('ureport.xml' in rep_name)-2) as name from ureport_file order by up_time desc")
    List<Map<String,String>> getReport();
}
