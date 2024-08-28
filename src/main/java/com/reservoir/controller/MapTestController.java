package com.reservoir.controller;


import com.reservoir.core.utils.MinMap;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/map")
public class MapTestController {
    @GetMapping("/test")
    public String test() {
        MinMap.set("name","zyh",1000L);

        System.out.println("key -----------------> "+MinMap.get("name"));
        return MinMap.get("name");
    }

    @GetMapping("/set/{key}")
    public String set(@PathVariable String key) {
        MinMap.set("name",key,60L);
        return MinMap.get("name");
    }

    @GetMapping("/get")
    public String get() {
        return MinMap.get("name");
    }
}
