package com.itheima.service;

import com.itheima.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 龚正宇
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2023-01-02 23:02:18
*/
public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);
}
