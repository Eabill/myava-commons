package com.myava.mybatis.tk;

import tk.mybatis.mapper.entity.Example;

/**
 * 动态表Example，用于动态表查询
 *
 * @author biao
 */
public class DynamicTableExample extends Example {

    private AbstractDynamicTable entity;

    public DynamicTableExample(AbstractDynamicTable entity) {
        super(entity.getClass());
        this.entity = entity;
    }

    @Override
    public String getDynamicTableName() {
        return entity.getDynamicTableName();
    }
}
