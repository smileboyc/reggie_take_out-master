package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pojo.DishFlavor;
import com.itheima.service.DishFlavorService;
import com.itheima.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author 龚正宇
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2022-12-28 22:23:23
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




