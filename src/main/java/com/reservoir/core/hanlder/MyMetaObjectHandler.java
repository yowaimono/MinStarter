package com.reservoir.core.hanlder;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        boolean createTime = metaObject.hasSetter("createdAt");
        boolean updateTime = metaObject.hasSetter("updatedAt");
        if (updateTime) {
            strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
        }
        if (createTime) {
            strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        boolean updateTime = metaObject.hasSetter("updateAt");
        if (updateTime) {
            strictUpdateFill(metaObject, "updateAt", LocalDateTime.class, LocalDateTime.now());
        }
    }
}