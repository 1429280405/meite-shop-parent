package com.mayikt.pay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liujinqiang
 * @create 2021-02-07 21:17
 */
@Controller
public class PayController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
