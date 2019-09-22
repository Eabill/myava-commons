package com.myava.springboot.service;

import com.myava.springboot.entity.User;

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
    List<User> selectAll();
}
