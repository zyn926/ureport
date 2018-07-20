/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mfd.dto.auUserInfo.AuUserInfo;
import com.mfd.service.auUserInfoService.AuUserInfoService;
import com.mfd.util.Md5Util;
import com.mfd.util.OperationLogUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@EnableAutoConfiguration
public class LoginController {

    @Autowired
    private AuUserInfoService auUserInfoService;

    @RequestMapping({"/","/index"})
    public ModelAndView index(){
        ModelAndView view = new ModelAndView("index");
        Subject subject = SecurityUtils.getSubject();
        AuUserInfo user = (AuUserInfo) subject.getPrincipal();
        List<Map<Object,Object>> menuList = auUserInfoService.getMenu(user.getUserId());
        String menus = JSON.toJSONString(menuList);
        view.addObject("menus",menus);
        return view;
    }

    @RequestMapping(value="/login",method= RequestMethod.GET)
    public String toLogin(){
        return "login";
    }

    /*@RequestMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        return "login";
    }*/

    @RequestMapping(value="/loginUser",method= RequestMethod.POST)
    public ModelAndView loginUser(@RequestParam("username") String username,
                                  @RequestParam("password") String password,
                                  HttpSession session, Map<String, Object> map) throws Exception{

        UsernamePasswordToken token = new UsernamePasswordToken(username, Md5Util.getMd5(password));
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            AuUserInfo user = (AuUserInfo) subject.getPrincipal();
            session.setAttribute("user", user);
            ModelAndView view = new ModelAndView("redirect:/index");
            //OperationLogUtil.operationLog(user.getName()+"登陆系统");
            return view;
        } catch (Exception e) {
            String msg = "";
            String exceptionName = e.getClass().getName();
            if (exceptionName.contains(AuthenticationException.class.getName())) {
                msg = "账号不存在：";
            } else if (exceptionName.contains(IncorrectCredentialsException.class.getName())) {
                System.out.println("IncorrectCredentialsException -- > 密码不正确：");
                msg = "密码不正确：";
            } else {
                msg = "else >> "+exceptionName;
                System.out.println("else -- >" + exceptionName);
            }
            map.put("msg",msg);
            return new ModelAndView("/login");
        }
        /*if (StringUtils.isNotBlank(msg)){
            map.put("msg", msg);
            return "/login";
        }
        return "/index";*/
    }

    @RequestMapping("/403")
    public String unauthorizedRole(){
        System.out.println("------没有权限-------");
        return "/error/403";
    }
}
