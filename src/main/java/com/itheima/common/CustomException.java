package com.itheima.common;
/**
 * @author 蒋樟
 * @date 2023/9/10 19:05
 * @description: 自定义异常类
 */

public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
