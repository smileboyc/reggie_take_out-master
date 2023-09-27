package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pojo.Employee;
import com.itheima.service.EmployeeService;
import com.itheima.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author 龚正宇
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2022-12-26 16:37:44
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




