package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.dto.DishDto;
import com.itheima.pojo.Dish;
import com.itheima.pojo.DishFlavor;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import com.itheima.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 龚正宇
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2022-12-28 19:43:25
*/
//事务注解
@Transactional
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //设置两张表
        this.save(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor list : flavors) {
            list.setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getWithFlavor(Long id) {
        Dish dish=this.getById(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors=dishFlavorService.list(lambdaQueryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }
    @Override
    public void updateWithFlavors(DishDto dishDto) {
        //先删除口味表里面对应数据，在插入数据
        this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(lambdaQueryWrapper);
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors=dishDto.getFlavors();
        for (DishFlavor list : flavors) {
            list.setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);



    }


}



