package com.myava.springboot.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 用户请求对象
 *
 * @author biao
 */
@Data
public class UserRequest implements Serializable {

    @NotEmpty(message = "ID列表不能为空")
    private List<Integer> idList;

    private static final long serialVersionUID = -7968252678108489821L;
}
