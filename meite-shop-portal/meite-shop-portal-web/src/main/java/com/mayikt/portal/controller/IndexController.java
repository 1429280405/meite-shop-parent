package com.mayikt.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liujinqiang
 * @create 2021-01-11 21:50
 */
@Controller
public class IndexController {
    /**
     * 跳转到首页
     *
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 跳转到首页
     *
     * @return
     */
    @RequestMapping("/index.html")
    public String indexHtml() {
        return index();
    }
}
