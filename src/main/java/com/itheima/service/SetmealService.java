package com.itheima.service;

import com.itheima.dto.SetmealDto;
import com.itheima.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 龚正宇
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2022-12-28 19:43:28
*/
public interface SetmealService extends IService<Setmeal> {

    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);
}
