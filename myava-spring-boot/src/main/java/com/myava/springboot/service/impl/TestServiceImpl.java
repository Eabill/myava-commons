package com.myava.springboot.service.impl;

import com.myava.springboot.mapper.TestMapper;
import com.myava.springboot.model.Test;
import com.myava.springboot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public List<Test> queryList() {
        return testMapper.selectAll();
    }
}
