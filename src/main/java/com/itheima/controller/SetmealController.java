package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.dto.SetmealDto;
import com.itheima.pojo.Category;
import com.itheima.pojo.Setmeal;
import com.itheima.pojo.SetmealDish;
import com.itheima.service.CategoryService;
import com.itheima.service.SetmealDishService;
import com.itheima.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    @CacheEvict(value="setmealCache",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
       setmealService.saveWithDish(setmealDto);
       return R.success("新增套餐成功");
    }
    //分页查询
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器对象
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage=new Page<>();
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime).like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        setmealService.page(pageInfo,lambdaQueryWrapper);

        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<Setmeal> list =pageInfo.getRecords();
        List<SetmealDto> list1=new ArrayList<>();
        for(Setmeal setmeal:list){
            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);
            Long categoryId=setmealDto.getCategoryId();
            Category category=categoryService.getById(categoryId);
            String categoryName=category.getName();
            setmealDto.setCategoryName(categoryName);
            list1.add(setmealDto);
        }
        setmealDtoPage.setRecords(list1);
        return R.success(setmealDtoPage);
    }
    @DeleteMapping
    @CacheEvict(value="setmealCache",allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }
    //键值对不能使用Requestbody
    @GetMapping("/list")
    @Cacheable(value="setmealCache",key="#setmeal.categoryId+'_'+#setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        objectLambdaQueryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        //增加排序条件
        List<Setmeal> list = setmealService.list(objectLambdaQueryWrapper);
        return R.success(list);
    }

}
