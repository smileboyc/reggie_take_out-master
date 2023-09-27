package com.itheima.service;

import com.itheima.dto.DishDto;
import com.itheima.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 龚正宇
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2022-12-28 19:43:25
*/
public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
    public DishDto getWithFlavor(Long id);

    public void updateWithFlavors(DishDto dishDto);
}
