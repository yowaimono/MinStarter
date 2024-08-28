//package com.reservoir.controller;
//
//import com.google.zxing.WriterException;
//import com.reservoir.core.utils.FileUtils;
//import com.reservoir.core.utils.QRCodeUtils;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.util.Base64;
//
//@RestController
//@RequestMapping("/qrcode")
//public class TestQRCodeController {
//
//    @GetMapping("/generate")
//    public ResponseEntity<String> generateQRCode(@RequestParam String text, @RequestParam int width, @RequestParam int height) {
//        try {
//
//            System.out.println(text);
//            byte[] qrCodeImage = QRCodeUtils.generateQRCodeImage(text);
//            String base64Image = FileUtils.getBytesBase64(qrCodeImage);
//
//            FileUtils.writeBase64ToFile(base64Image,"./qrcode.png");
//
//
//            return ResponseEntity.ok("data:image/png;base64," + base64Image);
//        } catch (WriterException | IOException e) {
//            return ResponseEntity.badRequest().body("Failed to generate QR code");
//        }
//    }
//}