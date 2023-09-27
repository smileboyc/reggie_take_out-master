package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pojo.AddressBook;
import com.itheima.service.AddressBookService;
import com.itheima.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author 龚正宇
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2022-12-30 18:47:56
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




