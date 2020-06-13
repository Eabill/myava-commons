package com.myava.springboot.controller;

import com.myava.base.RespResult;
import com.myava.springboot.domain.UserRequest;
import com.myava.springboot.domain.UserResponse;
import com.myava.springboot.entity.UserEntity;
import com.myava.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

/**
 * Api控制器
 *
 * @author biao
 */
@RestController
@RequestMapping("/api")
@Validated
public class ApiController {

    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    public RespResult<UserResponse> list() {
        return RespResult.success(new UserResponse(userService.selectAll()));
    }

    @GetMapping("/get")
    public RespResult<UserEntity> get(@Min(value = 1, message = "id:必须正整数") Integer id) {
        return RespResult.success(userService.get(id));
    }

    @RequestMapping(value = "/query")
    public RespResult<UserResponse> query(@Validated @RequestBody UserRequest request) {
        return RespResult.success(new UserResponse(userService.query(request)));
    }
}
