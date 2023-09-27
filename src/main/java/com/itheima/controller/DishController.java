package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.dto.DishDto;
import com.itheima.pojo.Category;
import com.itheima.pojo.Dish;
import com.itheima.pojo.DishFlavor;
import com.itheima.pojo.Employee;
import com.itheima.service.CategoryService;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 蒋樟
 * @date 2023/9/11 9:20
*/
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    //没有使用到的变量
    // @Autowired
    // private CacheManager cacheManager;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    //前端传回的JSON数据需要RequestBody反序列化到我们定义的实体对象中
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("新增菜品成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //可以使用连表查询
        //构造分页构造器对象
        Page<Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime).like(StringUtils.isNotEmpty(name), Dish::getName,name);
        dishService.page(pageInfo,lambdaQueryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records=pageInfo.getRecords();
        List<DishDto> list=new ArrayList<>();
        for (Dish dish:records) {
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(dish,dishDto);
            System.out.println(dish.getCategoryId());
            Long categoryId=dish.getCategoryId();
            Category category=categoryService.getById(categoryId);
            String categoryName=category.getName();
            dishDto.setCategoryName(categoryName);
            list.add(dishDto);
        }
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavors(dishDto);
        /*Set keys=redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);*/
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("修改菜品成功");
    }
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto=dishService.getWithFlavor(id);
        return R.success(dishDto);
    }
    /*@GetMapping("/list")
    //不通用
     //public R<List<Dish>> getdish(Dish dish) {
    public R<List<Dish>> getdish(Dish dish) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //等值查询
        lambdaQueryWrapper.eq(Dish::getCategoryId,dish.getCategoryId()).orderByAsc(Dish::getSort).eq(Dish::getStatus,1);
        List<Dish> list=dishService.list(lambdaQueryWrapper);
        return R.success(list);
    }*/
     @GetMapping("/list")
    public R<List<DishDto>> getdish(Dish dish) {

         String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
         List<DishDto> dishDtoList1=(List<DishDto>) redisTemplate.opsForValue().get(key);
         if(dishDtoList1!=null){
             return R.success(dishDtoList1);
         }
         LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
         lambdaQueryWrapper.eq(Dish::getCategoryId,dish.getCategoryId()).orderByAsc(Dish::getSort).eq(Dish::getStatus,1);
         List<Dish> list=dishService.list(lambdaQueryWrapper);
         List<DishDto> dishDtoList=new ArrayList<>();
         List<DishFlavor> dishFlavorList=new ArrayList<>();
         for (Dish dish1:list) {
             DishDto dishDto=new DishDto();
             BeanUtils.copyProperties(dish1,dishDto);
             Long categoryId=dish1.getCategoryId();
             Category category=categoryService.getById(categoryId);
             String categoryName=category.getName();
             dishDto.setCategoryName(categoryName);
             //当前菜品id
             Long dishId=dish1.getId();
             //查询口味表
             LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper1=new LambdaQueryWrapper<>();
             lambdaQueryWrapper1.eq(DishFlavor::getDishId,dishId);
             //返回List集合
             dishFlavorList=dishFlavorService.list(lambdaQueryWrapper1);
             dishDto.setFlavors(dishFlavorList);
             dishDtoList.add(dishDto);
         }
         redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
         return R.success(dishDtoList);
    }
}
