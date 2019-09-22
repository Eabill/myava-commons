package com.myava.springboot.service.impl;

import com.myava.springboot.entity.User;
import com.myava.springboot.mapper.UserMapper;
import com.myava.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }
}
