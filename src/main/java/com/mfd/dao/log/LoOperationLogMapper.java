package com.mfd.dao.log;

import com.mfd.dto.log.LoOperationLog;
import com.mfd.util.paging.PageQuery;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoOperationLogMapper {
    @Insert("insert into lo_operation_log (id,username,name,operation_content,create_time) values (#{id},#{username},#{name}," +
            "#{operationContent},now() )")
    int insert(LoOperationLog loOperationLog);

    @Select("<script> "+"select id,username,name,operation_content,create_time from lo_operation_log " +
            "<where><if test=\"page.param != null\"> <if test=\"page.param.name !=null and page.param.name != '' \"> name like concat('%',#{page.param.name},'%')</if> </if> </where> order by create_time desc LIMIT #{page.offset}, #{page.pageSize}"+"</script>")
    List<LoOperationLog> getPageList(@Param("page") PageQuery page);

    @Select("select count(1) from lo_operation_log")
    int count();
}
