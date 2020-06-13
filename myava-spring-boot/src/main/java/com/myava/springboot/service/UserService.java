package com.myava.springboot.service;

import com.myava.springboot.domain.UserRequest;
import com.myava.springboot.entity.UserEntity;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author biao
 */
public interface UserService {

    /**
     * 查询全部记录
     * @return
     */
    List<UserEntity> selectAll();

    /**
     * 根据ID获取对象
     * @param id
     * @return
     */
    UserEntity get(Integer id);

    /**
     * 查询列表
     * @param userRequest
     * @return
     */
    List<UserEntity> query(UserRequest userRequest);
}
