package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.CustomException;
import com.itheima.pojo.Category;
import com.itheima.pojo.Dish;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CategoryService;
import com.itheima.mapper.CategoryMapper;
import com.itheima.service.DishService;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 蒋樟
 * @date 2023/9/10 18:38
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        lambdaQueryWrapper.eq(Dish::getCategoryId,id);
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个异常
        int count= dishService.count(lambdaQueryWrapper);
        if(count>0){
            //自定义异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Setmeal::getCategoryId,id);
        int count1= setmealService.count(lambdaQueryWrapper1);
        if(count1>0){
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除
        super.removeById(id);
    }
}




