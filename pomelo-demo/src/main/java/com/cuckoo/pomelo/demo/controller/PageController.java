package com.cuckoo.pomelo.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面路由控制器
 */
@Controller
public class PageController {
    
    /**
     * 首页
     */
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
    
    /**
     * 数据源配置页面
     */
    @GetMapping("/pomelo/settings")
    public String settings() {
        return "forward:/settings.html";
    }
}