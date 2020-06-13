package com.myava.springboot.service.impl;

import com.myava.springboot.domain.UserRequest;
import com.myava.springboot.entity.UserEntity;
import com.myava.springboot.mapper.UserMapper;
import com.myava.springboot.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 用户服务实现类
 *
 * @author biao
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserEntity> selectAll() {
        return userMapper.selectAll();
    }

    @Override
    public UserEntity get(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UserEntity> query(UserRequest userRequest) {
        Example example = new Example(UserEntity.class);
        example.orderBy("id").asc();
        Example.Criteria criteria = example.createCriteria();
        if (CollectionUtils.isNotEmpty(userRequest.getIdList())) {
            criteria.andIn("id", userRequest.getIdList());
        }
        return userMapper.selectByExample(example);
    }
}
