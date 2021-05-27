package com.snow.storeapi.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.snow.storeapi.entity.DictItem;
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
public class DictItemHandler extends BaseTypeHandler<List<DictItem>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<DictItem> dictItems, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSON.toJSONString(dictItems));
    }

    @Override
    public List<DictItem> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String sqlJson = resultSet.getString(s);
        if (null != sqlJson) {
            return JSONArray.parseArray(sqlJson, DictItem.class);
        }
        return null;
    }

    @Override
    public List<DictItem> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String sqlJson = resultSet.getString(i);
        if (null != sqlJson) {
            return JSONArray.parseArray(sqlJson, DictItem.class);
        }
        return null;
    }

    @Override
    public List<DictItem> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String sqlJson = callableStatement.getString(i);
        if (null != sqlJson) {
            return JSONArray.parseArray(sqlJson, DictItem.class);
        }
        return null;
    }
}
