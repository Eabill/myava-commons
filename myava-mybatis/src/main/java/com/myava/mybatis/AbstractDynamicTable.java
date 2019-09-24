package com.myava.mybatis;

import com.myava.mybatis.annotation.DynamicTable;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.entity.IDynamicTableName;

import javax.persistence.Transient;
import java.lang.reflect.Field;

/**
 * 动态表名抽象类
 *
 * @author biao
 */
public abstract class AbstractDynamicTable implements IDynamicTableName {

    @Setter
    @Getter
    @Transient
    protected String tableName;

    @Override
    public String getDynamicTableName() {
        try {
            DynamicTable dynamicTable = this.getClass().getAnnotation(DynamicTable.class);
            Field field = this.getClass().getDeclaredField(dynamicTable.field());
            field.setAccessible(true);
            if (tableName != null || field.get(this) == null) {
                return tableName;
            }
            int tabInx = Math.abs(field.get(this).hashCode()) % dynamicTable.capacity();
            return dynamicTable.prefix() + dynamicTable.symbol() + tabInx;
        } catch (Exception ex) {
            throw new RuntimeException("DynamicTableName get error.", ex);
        }
    }
}
