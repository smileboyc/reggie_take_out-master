package com.itheima.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void test(){
       /* ValueOperations valueOperations=redisTemplate.opsForValue();
        valueOperations.set("city123","yunnan");
        String value=(String)redisTemplate.opsForValue().get("city123");
        System.out.println(value);*/
        redisTemplate.opsForHash().put("002","name","xiaoming");
        redisTemplate.opsForHash().put("002","age","20");
        redisTemplate.opsForHash().put("002","address","bj");

    }
}
