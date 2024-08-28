package com.reservoir.controller;

import com.reservoir.core.utils.BloomFilterUtil;
import com.reservoir.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
@RestController
public class BloomFilterController {

//    @Autowired
//    private final BloomFilterUtil bloomFilterUtil = BloomFilterUtil.getBF("test");

    @Autowired
    private Map<String, Object> reservoirInfoMap;

    @GetMapping("/add")
    public String add(@RequestParam String item) {
        BloomFilterUtil.getBF("test").set(item);
        BloomFilterUtil.getBF("set1").set("qwm");

        BloomFilterUtil.setDefault("你好");

        return "Added: " + item;
    }

    @GetMapping("/contains")
    public String contains(@RequestParam String item) {
        Boolean result = BloomFilterUtil.getBF("test").contains(item);

        return "Contains: " + item + " -> " + result;
    }

    @Autowired
    UserMapper userMapper;

//    @GetMapping("/mm")
//    public String mm(@RequestParam String item) {
//        User res = Min.select(User.Username()).where(User.Email(),"123456@qq.com").first(userMapper);
//
//        return res.toString();
//    }
}