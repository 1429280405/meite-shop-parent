package com.mayikt.pay.callback.service;

import com.mayikt.pay.callback.template.AbstractPayCallbackTemplate;
import com.mayikt.pay.callback.template.factory.TemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liujinqiang
 * @create 2021-02-24 0:21
 */
@RestController
public class PayAsynCallbackService {
    private static final String UNIONPAYCALLBACK_TEMPLATE = "unionPayCallbackTemplate";


    @RequestMapping("/unionPayAsynCallback")
    public String unionPayAsynCallback(HttpServletRequest req, HttpServletResponse resp){
        AbstractPayCallbackTemplate payCallbackTemplate = TemplateFactory.getPayCallbackTemplate(UNIONPAYCALLBACK_TEMPLATE);
        return payCallbackTemplate.asyCallBack(req,resp);
    }


















}
