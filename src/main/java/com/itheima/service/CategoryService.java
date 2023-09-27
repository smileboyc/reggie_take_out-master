package com.itheima.service;

import com.itheima.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 蒋樟
 * @date 2023/9/10 18:38
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service
*/
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
