package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.common.R;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    @RequestMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession){
        String phone=user.getPhone();
        if(StringUtils.isNotEmpty((phone))){
            String code= ValidateCodeUtils.generateValidateCode(4).toString();
            log.info(code);
            SMSUtils.sendMessage("西瓜外卖","SMS_287675779",phone,code);
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.success("手机验证码短信发送成功");
        }
        return R.error("手机验证码短信发送失败");

    }
    @RequestMapping("/login")
    public R<User> sendMsg(@RequestBody Map map, HttpSession httpSession){
        String phone=map.get("phone").toString();
        String code=map.get("code").toString();
        Object codeInSession=redisTemplate.opsForValue().get(phone);
        if(codeInSession!=null&&codeInSession.equals(code)){
            LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone,phone);
             User user=userService.getOne(lambdaQueryWrapper);
             if(user==null){
                user=new User();
                user.setPhone(phone);
                userService.save(user);
             }
             httpSession.setAttribute("user",user.getId());
             redisTemplate.delete(phone);
             return R.success(user);
        }
        return R.error("登录失败");

    }
}
