package com.reservoir.core.utils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Min<T, M extends BaseMapper<T>> {

    private final QueryWrapper<T> queryWrapper;
    private final UpdateWrapper<T> updateWrapper;
    private boolean orderByCalled = false;
    private boolean limitCalled = false;
    private T entity;
    private List<T> entityList;

    private Min() {
        this.queryWrapper = new QueryWrapper<>();
        this.updateWrapper = new UpdateWrapper<>();
    }

    public static <T, M extends BaseMapper<T>> Min<T, M> select(String... columns) {
        Min<T, M> min = new Min<>();
        if (columns != null && columns.length > 0) {
            min.queryWrapper.select(columns);
        }
        return min;
    }

    public static <T, M extends BaseMapper<T>> Min<T, M> insert(T entity) {
        Min<T, M> min = new Min<>();
        min.entity = entity;
        return min;
    }

    public static <T, M extends BaseMapper<T>> Min<T, M> update() {
        return new Min<>();
    }

    public static <T, M extends BaseMapper<T>> Min<T, M> delete() {
        return new Min<>();
    }

    // Query Methods
    public Min<T, M> innerJoin(String joinTable, String onClause) {
        return join("INNER JOIN", joinTable, onClause);
    }

    public Min<T, M> leftJoin(String joinTable, String onClause) {
        return join("LEFT JOIN", joinTable, onClause);
    }

    public Min<T, M> rightJoin(String joinTable, String onClause) {
        return join("RIGHT JOIN", joinTable, onClause);
    }

    private Min<T, M> join(String joinType, String joinTable, String onClause) {
        Objects.requireNonNull(joinTable, "joinTable must not be null");
        Objects.requireNonNull(onClause, "onClause must not be null");
        this.queryWrapper.last(joinType + " " + joinTable + " ON " + onClause);
        return this;
    }

    public Min<T, M> where(String column, Object value) {
        return condition(column, value, true);
    }

    public Min<T, M> and(String column, Object value) {
        return condition(column, value, true);
    }

    public Min<T, M> or(String column, Object value) {
        return condition(column, value, false);
    }

    private Min<T, M> condition(String column, Object value, boolean isAnd) {
        Objects.requireNonNull(column, "column must not be null");
        Objects.requireNonNull(value, "value must not be null");
        if (isAnd) {
            this.queryWrapper.eq(column, value);
            this.updateWrapper.eq(column, value);
        } else {
            this.queryWrapper.or().eq(column, value);
            this.updateWrapper.or().eq(column, value);
        }
        return this;
    }

    public Min<T, M> like(String column, Object value) {
        Objects.requireNonNull(column, "column must not be null");
        Objects.requireNonNull(value, "value must not be null");
        this.queryWrapper.like(column, value);
        this.updateWrapper.like(column, value);
        return this;
    }

    public Min<T, M> between(String column, Object value1, Object value2) {
        Objects.requireNonNull(column, "column must not be null");
        Objects.requireNonNull(value1, "value1 must not be null");
        Objects.requireNonNull(value2, "value2 must not be null");
        this.queryWrapper.between(column, value1, value2);
        this.updateWrapper.between(column, value1, value2);
        return this;
    }

    public Min<T, M> in(String column, List<?> values) {
        Objects.requireNonNull(column, "column must not be null");
        Objects.requireNonNull(values, "values must not be null");
        if (values.isEmpty()) {
            throw new IllegalArgumentException("values must not be empty");
        }
        this.queryWrapper.in(column, values);
        this.updateWrapper.in(column, values);
        return this;
    }

    public Min<T, M> orderBy(boolean isAsc, String... columns) {
        if (orderByCalled) {
            throw new IllegalStateException("orderBy has already been called");
        }
        orderByCalled = true;
        if (columns != null && columns.length > 0) {
            this.queryWrapper.orderBy(true, isAsc, Arrays.asList(columns));
        }
        return this;
    }

    public Min<T, M> groupBy(String... columns) {
        if (columns != null && columns.length > 0) {
            this.queryWrapper.groupBy(Arrays.asList(columns));
        }
        return this;
    }

    public Min<T, M> limit(int offset, int size) {
        if (limitCalled) {
            throw new IllegalStateException("limit has already been called");
        }
        limitCalled = true;
        if (offset < 0 || size <= 0) {
            throw new IllegalArgumentException("Offset must be >= 0 and size must be > 0");
        }
        this.queryWrapper.last("LIMIT " + offset + ", " + size);
        return this;
    }

    public Min<T, M> having(String column, Object value) {
        Objects.requireNonNull(column, "column must not be null");
        Objects.requireNonNull(value, "value must not be null");
        this.queryWrapper.having(column, value);
        return this;
    }

    public Min<T, M> isNull(String column) {
        Objects.requireNonNull(column, "column must not be null");
        this.queryWrapper.isNull(column);
        this.updateWrapper.isNull(column);
        return this;
    }

    public Min<T, M> isNotNull(String column) {
        Objects.requireNonNull(column, "column must not be null");
        this.queryWrapper.isNotNull(column);
        this.updateWrapper.isNotNull(column);
        return this;
    }

    public <T> T first(BaseMapper mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        return (T) mapper.selectOne((Wrapper<T>) this.queryWrapper);
    }

    public List<T> list(BaseMapper mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        return mapper.selectList((Wrapper<T>) this.queryWrapper);
    }

    // Insert Methods
    public Min<T, M> one() {
        Objects.requireNonNull(this.entity, "entity must not be null");
        return this;
    }

    public Min<T, M> list(List<T> entities) {
        Objects.requireNonNull(entities, "entities must not be null");
        if (entities.isEmpty()) {
            throw new IllegalArgumentException("entities must not be empty");
        }
        this.entityList = entities;
        return this;
    }

    public int InsertRows(BaseMapper mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        if (this.entity != null) {
            return mapper.insert(this.entity);
        } else if (this.entityList != null) {
            int count = 0;
            for (T e : this.entityList) {
                count += mapper.insert(e);
            }
            return count;
        } else {
            throw new IllegalStateException("No entity or entityList provided for insert");
        }
    }

    // Update Methods
    public Min<T, M> set(String column, Object value) {
        Objects.requireNonNull(column, "column must not be null");
        Objects.requireNonNull(value, "value must not be null");
        this.updateWrapper.set(column, value);
        return this;
    }

    public int UpdateRows(BaseMapper mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        if (this.updateWrapper.isEmptyOfWhere()) {
            throw new IllegalStateException("Update operation must contain at least one where clause");
        }
        return mapper.update(null, this.updateWrapper);
    }

    // Delete Methods
    public int DeleteRows(BaseMapper mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        if (this.queryWrapper.isEmptyOfWhere()) {
            throw new IllegalStateException("Delete operation must contain at least one where clause");
        }
        return mapper.delete((Wrapper<T>) this.queryWrapper);
    }

    // Utility Methods
    public Min<T, M> setFields(Map<String, Object> fieldValues) {
        Objects.requireNonNull(fieldValues, "fieldValues must not be null");
        fieldValues.forEach((key, value) -> {
            Objects.requireNonNull(key, "field key must not be null");
            Objects.requireNonNull(value, "field value must not be null");
            this.updateWrapper.set(key, value);
        });
        return this;
    }
}