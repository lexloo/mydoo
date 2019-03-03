package com.open.capacity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2018年4月5日 下午19:52:21
 */
@RestController
public class TestController {
//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/test111")
    public String hello() {
        return "hello";
    }

}
