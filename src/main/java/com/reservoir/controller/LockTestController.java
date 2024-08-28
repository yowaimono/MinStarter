//package com.reservoir.controller;
//
//import com.reservoir.core.utils.RedisFairLock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.concurrent.TimeUnit;
//
//@RestController
//public class LockTestController {
//
//    @Autowired
//    private RedisFairLock redisFairLock;
//
//    @GetMapping("/testLock")
//    public String testLock(@RequestParam String lockKey, @RequestParam long waitTime, @RequestParam long leaseTime) {
//        try {
//            boolean locked = redisFairLock.tryLock(lockKey, waitTime, leaseTime, TimeUnit.MILLISECONDS);
//            if (locked) {
//                try {
//                    // 模拟一些业务操作
//                    Thread.sleep(20000);
//                    return "Lock acquired and operation completed successfully.";
//                } finally {
//                    redisFairLock.unlock(lockKey);
//                }
//            } else {
//                return "Failed to acquire lock.";
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            return "Interrupted while trying to acquire lock.";
//        }
//    }
//}