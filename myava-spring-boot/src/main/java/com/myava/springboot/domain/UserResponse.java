package com.myava.springboot.domain;

import com.myava.springboot.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 用户响应对象
 *
 * @author biao
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse implements Serializable {

    /**
     * 用户列表
     */
    private List<UserEntity> userList;

    private static final long serialVersionUID = 8428204634954911293L;
}
