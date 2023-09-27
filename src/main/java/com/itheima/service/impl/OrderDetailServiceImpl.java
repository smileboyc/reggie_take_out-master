package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pojo.OrderDetail;
import com.itheima.service.OrderDetailService;
import com.itheima.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author 龚正宇
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2023-01-02 23:02:24
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




