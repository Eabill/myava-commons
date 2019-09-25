package com.myava.mybatis.common;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 基础mapper接口，继承tk-mybatis通用mapper接口
 * @param <T>
 *
 * @author biao
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
