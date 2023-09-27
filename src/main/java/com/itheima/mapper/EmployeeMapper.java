package com.itheima.mapper;

import com.itheima.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
* @author 蒋樟
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2022-12-26 16:37:44
* @Entity com.itheima.pojo.Employee
*/
public interface EmployeeMapper extends BaseMapper<Employee> {

}




