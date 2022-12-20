package com.snow.storeapi.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snow.storeapi.entity.DictItem;
import com.snow.storeapi.util.MyUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class DictItemTypeHandler extends BaseTypeHandler<Object> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object object, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setString(i, MyUtils.objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String sqlJson = resultSet.getString(s);
        return MyUtils.parseJson(sqlJson, DictItem.class);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String sqlJson = resultSet.getString(i);
        return MyUtils.parseJson(sqlJson, DictItem.class);
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String sqlJson = callableStatement.getString(i);
        return MyUtils.parseJson(sqlJson, DictItem.class);
    }
}
