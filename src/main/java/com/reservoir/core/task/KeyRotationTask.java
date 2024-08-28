//package com.reservoir.core.task;
//
//import com.reservoir.core.utils.AESUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class KeyRotationTask {
//
//    @Autowired
//    private AESUtil aesUtil;
//
//    // 每30天更换一次密钥和IV
//    @Scheduled(fixedRate = 30L * 24 * 60 * 60 * 1000)
//    public void rotateKeyAndIV() {
//        try {
//            aesUtil.generateKeyAndIV();
//            System.out.println("New key and IV generated.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}