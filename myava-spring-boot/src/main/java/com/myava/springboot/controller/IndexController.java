package com.myava.springboot.controller;

import com.myava.springboot.entity.UserEntity;
import com.myava.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 入口控制器
 *
 * @author biao
 */
@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<UserEntity> list() {
        return userService.selectAll();
    }

}
