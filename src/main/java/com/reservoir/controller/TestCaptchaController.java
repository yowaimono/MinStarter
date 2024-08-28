package com.reservoir.controller;

import com.reservoir.core.annotation.Auth;
import com.reservoir.core.entity.Result;
import com.reservoir.core.entity.ResultCode;
import com.reservoir.core.utils.AESUtil;
import com.reservoir.core.utils.CaptchaUtils;
import com.reservoir.core.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/captcha")
public class TestCaptchaController {

    @GetMapping("/numeric")
    public ResponseEntity<String> getNumericCaptcha(@RequestParam int length) {
        try {
            byte[] captchaImage = CaptchaUtils.generateNumericCaptcha(length);
            String base64Image = Base64.getEncoder().encodeToString(captchaImage);

            FileUtils.writeBase64ToFile(base64Image,"./image.png");
            return ResponseEntity.ok("data:image/png;base64," + base64Image);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to generate captcha");
        }
    }

    @GetMapping("/alphabetic")
    public ResponseEntity<String> getAlphabeticCaptcha(@RequestParam int length) {
        try {
            byte[] captchaImage = CaptchaUtils.generateAlphabeticCaptcha(length);
            String base64Image = Base64.getEncoder().encodeToString(captchaImage);

            FileUtils.writeBase64ToFile(base64Image,"./image.png");
            return ResponseEntity.ok("data:image/png;base64," + base64Image);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to generate captcha");
        }
    }

    @GetMapping("/mixed")
    public ResponseEntity<String> getMixedCaptcha(@RequestParam int length) {
        try {
            byte[] captchaImage = CaptchaUtils.generateMixedCaptcha(length);
            String base64Image = Base64.getEncoder().encodeToString(captchaImage);

            FileUtils.writeBase64ToFile(base64Image,"./image.png");
            return ResponseEntity.ok("data:image/png;base64," + base64Image);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to generate captcha");
        }
    }

    @Autowired
    AESUtil aesUtil;

    @GetMapping("/ser")
    public Result<?> ttt() throws Exception {
        try {
            String name = "qwm";
            String oname = aesUtil.encrypt(name);
            String sname = aesUtil.decrypt(oname);
            System.out.println(sname);
            return Result.success(sname);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Result.of(ResultCode.FORBIDDEN_REQUEST);
        }

    }
}