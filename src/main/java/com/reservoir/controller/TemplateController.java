package com.reservoir.controller;

import org.springdoc.webmvc.ui.SwaggerUiHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class TemplateController {


    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("message", "Hello, Spring MVC!");
        return  "home"; // 返回视图名称
    }

    @GetMapping("/click")
    public String click(Model model) {
        return "click";
    }

}