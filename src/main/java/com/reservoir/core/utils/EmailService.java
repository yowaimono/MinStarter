package com.reservoir.core.utils;//package com.reservoir.core.utils;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.spring6.SpringTemplateEngine;
//
//import java.util.Random;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender emailSender;
//
//
//    @Autowired
//    private SpringTemplateEngine templateEngine;
//
//
//    public void sendSuccessInfo(String to, String name) {
//
//        Context context = new Context();
//        context.setVariable("userName", name);
//
//        String body = templateEngine.process("register-success", context);
//
//        try {
//
//            MimeMessage message = emailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setFrom("itakeaway@163.com");
//            helper.setTo(to);
//            helper.setSubject("欢迎使用iTakeaway");
//            helper.setText(body, true);
//            emailSender.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    public void sendVerificationCode(String to, String code) {
//
//        Context context = new Context();
//        context.setVariable("verificationCode", code);
//
//        String body = templateEngine.process("template-code", context);
//
//        try {
//
//            MimeMessage message = emailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setFrom("itakeaway@163.com");
//            helper.setTo(to);
//            helper.setSubject("iTakeaway注册验证码");
//            helper.setText(body, true);
//            emailSender.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String generateVerificationCode(int length) {
//        StringBuilder sb = new StringBuilder();
//        Random random = new Random();
//        for (int i = 0; i < length; i++) {
//            sb.append(random.nextInt(10));
//        }
//        return sb.toString();
//    }
//
//
//
//    public void sendEmail(String to, String subject, String content) throws MessagingException {
//
//        MimeMessage message = emailSender.createMimeMessage();
//
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//        helper.setTo(to);
//        helper.setFrom("itakeaway@163.com");
//
//        helper.setSubject(subject);
//
//        helper.setText(content, true);
//
//        emailSender.send(message);
//
//    }
//
//}