package com.mfd.dao.auUserInfo;

import com.mfd.dto.auUserInfo.AuUserInfo;
import com.mfd.dto.permission.Permission;
import com.mfd.dto.roleInfo.RoleInfo;
import com.mfd.util.paging.PageQuery;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface AuUserInfoMapper {

    /*
        登陆操作
     */
    @Select("select user_id,user_name,password,name,create_time,up_time from  au_user where user_name=#{usrName}")
    AuUserInfo getUserByName(String usrName);

    @Select("select t1.role_id,role_name,description from  au_role t1,au_user_role t2 where t1.role_id=t2.role_id and t2.user_id=#{usrId}")
    List<RoleInfo> getRolesByUseId(Integer usrId);

    @Select("select t1.per_id,per_name,per_url,fid from  au_permission t1 ,au_role_permission t2 where t1.per_id=t2.per_id and t2.role_id=#{roleId}")
    List<Permission>  getPermissonByRoleId(Integer roleId);

    @Select("select t.per_id as id,t.fid as pid,t.per_name as name,t.icon,t.per_url as url from au_permission t left join au_role_permission t1 on t.per_id=t1.per_id left join au_user_role t2 on t2.role_id=t1.role_id where t2.user_id = #{userId} group by t.fid,t.per_id,t.per_name,t.icon,t.per_url order by t.per_name ")
    List<Map<Object,Object>> getMenu(@Param("userId") Integer userId);

    /*
        用户维护
     */
    @Select("select u.user_id,u.user_name,u.name,u.create_time,t.role_names from au_user u left join (select string_agg(r.role_name,',') as role_names,aur.user_id from au_user_role aur left join au_role r on aur.role_id=r.role_id group by aur.user_id) t on u.user_id=t.user_id order by create_time desc limit #{page.pageSize} offset #{page.offset} ")
    List<AuUserInfo> getPageList(@Param("page") PageQuery page);

    /**
     * 统计
     * @param param
     * @return
     */
    @Select("select count(user_id) from au_user")
    int count(@Param("param")Map param);

    /**
     * 用于添加时验证是否已存在用户
     * @param userName
     * @return
     */
    @Select("select count(user_id) from au_user where user_name = #{userName}")
    int selectByName(@Param("userName") String userName);

    /**
     * 新增用户，返回新增的id
     * @param user
     * @return
     */
    //@Options(useGeneratedKeys=true, keyProperty="userId")
    @SelectKey(statement="select last_value from au_user_user_id_seq",keyProperty = "userId",before=false, resultType=Integer.class)
    @Insert("insert into au_user (user_name,name,password,create_time) values (#{userName},#{name},#{password},now())")
    int insert(AuUserInfo user);

    /**
     * 单个或批量删除
     * @param userId
     * @return
     */
   /* @Delete("<script> "+"delete from au_user where user_id in <foreach item = \"item\" index = \"index\" collection = \"userIds\" open = \"(\" separator = \",\" close = \")\"> #{item} </foreach>"+"</script>")*/
    @Delete("delete from au_user where user_id = #{userId}")
    int deleteById(@Param("userId") Integer userId);

    /**
     * 查询所有角色
     * @return
     */
    @Select("select role_id,role_name from au_role order by role_name desc")
    List<RoleInfo> selectAllRole();

    /**
     * 查询用户已有的角色
     * @param userId
     * @return
     */
    @Select("select string_agg(cast(r.role_id as text),',') as \"roleIds\",string_agg(r.role_name,',') as \"roleNames\",u.user_name as \"userName\",u.name from au_user u left join au_user_role aur on aur.user_id=u.user_id left join au_role r on r.role_id=aur.role_id where u.user_id = #{userId} group by u.user_name,u.name")
    Map selectRoleByUser(@Param("userId") String userId);

    /**
     * 用户角色关系添加
     * @param userRole
     * @return
     */
    @Insert("<script>"+" insert into au_user_role (user_id,role_id) values <foreach item=\"item\" index = \"index\" collection = \"userRole\" separator = \",\"> (#{item.userId},#{item.roleId})</foreach>"+"</script>")
    int insertUserRole(@Param("userRole") List userRole);

    /**
     * 删除用户原有的关系
     * @param userId
     * @return
     */
    @Delete("delete from au_user_role where user_id = #{userId}")
    int deleteUserRole(@Param("userId") String userId);

    /**
     * 删除用户时删除关系表中的关系
     * @param userId
     * @return
     */
    /*@Delete("<script>"+"delete from au_user_role where user_id in <foreach item = \"item\" index = \"index\" collection = \"userIds\" open = \"(\" separator = \",\" close = \")\"> #{item} </foreach>"+"</script>")*/
    @Delete("delete from au_user_role where user_id = #{userId} ")
    int deleteUserAndRole(@Param("userId") Integer userId);

    /**
     * 验证用户原密码
     * @param userId
     * @param pass
     * @return
     */
    @Select("select count(user_id) from au_user where user_id = #{userId} and password = #{pass}")
    int checkPass(@Param("userId") Integer userId,@Param("pass") String pass);

    /**
     * 修改密码
     * @param userId
     * @param pass
     * @return
     */
    @Update("update au_user set password = #{pass} where user_id = #{userId}")
    int updatePass(@Param("userId") Integer userId,@Param("pass") String pass);
}
