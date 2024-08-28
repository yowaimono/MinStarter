//package com.reservoir.controller;
//
//import cn.hutool.core.date.DateTime;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.reservoir.core.annotation.Auth;
//import com.reservoir.core.annotation.RateLimit;
//import com.reservoir.core.entity.Result;
//import com.reservoir.core.entity.ResultCode;
//import com.reservoir.core.utils.*;
//import com.reservoir.entity.user.User;
//
//import com.reservoir.mapper.UserMapper;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
////
////import jakarta.mail.MessagingException;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//
//import org.apache.tools.ant.taskdefs.Get;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.AsyncResult;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.*;
//import java.util.concurrent.*;
//
//@RestController
//@RequestMapping(value = "/test")
//@Tag(name = "测试接口")
//@Slf4j
//public class TestController {
//
//    @Autowired
//    private JwtUtils jwtUtil;
//
////    @Autowired
////    EmailService emailService;
//
//    @Autowired
//    private UserMapper userMapper;
//
////    @Autowired
////    private RedisUtils redisUtils;
//
//    @RequestMapping(value="/doc",method = { RequestMethod.POST, RequestMethod.GET })
//    public  ModelAndView redirect(String userName){
//        ModelAndView  model = new ModelAndView("redirect:/swagger-ui/index.html");
//        return model;
//    }
//
//    @RateLimit
//    @Operation(summary = "一个简单的测试接口")
//    @GetMapping("/hello/{name}")
//    @Auth
//    public Result<?> testHello(@PathVariable("name")String name){
//        return Result.of(ResultCode.SUCCESS,"你好啊"+name);
//    }
//
//
//    @Operation(summary = "测试JWT")
//    @PostMapping("/login")
//    public Result<?> login(@RequestParam String username, @RequestParam String password) {
//        // 验证用户名和密码
//        if ("your_username".equals(username) && "your_password".equals(password)) {
//            String token = jwtUtil.generateToken(Map.of("username",username));
//            String name = jwtUtil.extractUsername(token);
//            Date time = jwtUtil.extractExpiration(token);
//            return Result.success(Map.of("token",token,"name",name,"time",time));
//        } else {
//            return Result.of(ResultCode.LOGIN_FAILED);
//        }
//    }
//
////    @Operation(summary = "testRedis")
////    @GetMapping("/testRedis")
////    @Async(value = "MinPool")
////    public CompletableFuture<Result<?>> testRedis() {
////        redisUtils.set("name", "zyh");
////        log.info("你好啊敏");
////        return CompletableFuture.completedFuture(Result.success(null));
////    }
//
//
//    @Operation(summary = "testUser")
//    @PostMapping("/user")
//    public Result<?> testUser(@Valid @RequestBody User user) {
////        if (bindingResult.hasErrors()) {
////            return Result.error(400,"验证错误！",bindingResult.getAllErrors());
////        }
//
//        userMapper.insert(user);
//        return Result.success(user);
//    }
//
//
//    @Operation(summary = "testMin")
//    @PostMapping("/min")
//    public Result<?> testMin() {
////        if (bindingResult.hasErrors()) {
////            return Result.error(400,"验证错误！",bindingResult.getAllErrors());
////        }
//        //User u = new User().setEmail("saohsaih").setUsername("qwm").setPassword("sasas").setCreatedAt(new Date(String.valueOf(DateTime.now())));
////         Min.insert(user).one().executeInsert(userMapper);
//        //Min.update().set("username","zyzzzz").where("id",1).executeUpdate(userMapper);
//        //User u =Min.select("id","username").where("username","s1 asa").and("email","string").first(userMapper);
//         //List<?> u = Min.select().orderBy(false,"id").list(userMapper);
//        //Min.delete().where("id",1).executeDelete(userMapper);
//        User u = Min.select().where("username","sg").first(userMapper);
//        return Result.success(u);
//    }
//
//    @Operation(summary = "查IP")
//    @GetMapping("/getaddr")
//    public Result<?> getaddr(String ipaddr) {
//        return Result.success(IpUtils.getCityInfo(ipaddr));
//    }
//
//
//    @Operation(summary = "检测四乱")
//    @GetMapping("/detect")
//    public Result<?> detect(@RequestParam("uid") Long uid) throws IOException {
//        // 创建一个包含图片和经纬度的响应
//        Map<String, Object> response = new HashMap<>();
//        response.put("image", "test.png");
//        response.put("location", getLocation());
//
//        return Result.success(response);
//    }
//
//    private Map<String, Object> getLocation() {
//        // 这里可以添加逻辑来获取经纬度信息
//        Map<String, Object> location = new HashMap<>();
//        location.put("latitude", "28.686666666666667");
//        location.put("longitude", "116.02166666666666");
//        return location;
//    }
//
//
////    @Operation(summary = "测试邮箱")
////    @GetMapping(value = "/mail")
////    public Result<?> mail(@RequestParam("email") String email) throws MessagingException {
////        emailService.sendEmail("3485000346@qq.com","你好","傻逼卢金荣");
////
////        return Result.success(null);
////    }
//
//
//}
