package com.mfd.ureport.dao;

import com.mfd.ureport.dto.UreportFileEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UreportFileMapper {

    /**
     *  根据报表名称检查报表是否存在
     * @param name 报表名称
     * @return
     */
    @Select("select count(*) from ureport_file where rep_name = #{name}")
    int checkExistByName(@Param("name") String name);

    /**
     *  根据报表名称查询报表
     * @param name 报表名称
     * @return
     */
    @Select("select rep_id,rep_name,rep_content,create_time,up_time from ureport_file where rep_name = #{name}")
    UreportFileEntity queryUreportFileEntityByName(@Param("name") String name);

    /**
     * 查询全部报表
     * @return
     */
    @Select("select rep_id,rep_name,rep_content,create_time,up_time from ureport_file order by create_time desc")
    List<UreportFileEntity> queryReportFileList();

    /**
     * 根据报表名称删除报表
     * @param name
     * @return
     */
    @Delete("delete from ureport_file where rep_name = #{name}")
    int deleteReportFileByName(@Param("name") String name);


    /**
     *  保存报表
     */
    @Insert("insert into ureport_file (rep_name, rep_content, create_time,up_time) values (#{repName}, #{repContent},now(),now())")
    int insertReportFile(UreportFileEntity entity);

    /**
     *  更新报表
     * @param entity
     * @return
     */
    @Update("<script> update ureport_file set up_time = now() <if test=\"repName != null and repName != ''\"> ,rep_name = #{repName}</if> <if test=\"repContent != null and repContent != ''\"> ,rep_content = #{repContent}</if>  where  rep_id = #{repId} </script>")
    int updateReportFile(UreportFileEntity entity);

}
