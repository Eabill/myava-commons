package com.myava.springboot.controller;

import com.myava.springboot.model.Test;
import com.myava.springboot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController {

    @Autowired
    private TestService testService;

    @RequestMapping("/")
    public String index() {
        return "Hello, spring boot!";
    }

    @RequestMapping("/list")
    public List<Test> list() {
        return testService.queryList();
    }

}
