package com.snow.storeapi.config;

import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(JSONArray.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class MySqlJsonArrayHandler extends BaseTypeHandler<JSONArray> {

    /**
     * 设置非空参数
     * @param preparedStatement
     * @param i
     * @param objects
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, JSONArray objects, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, objects.toJSONString());
    }

    /**
     * 根据列名，获取可以为空的结果
     * @param resultSet
     * @param s
     * @return
     * @throws SQLException
     */
    @Override
    public JSONArray getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String sqlJson = resultSet.getString(s);
        if (null != sqlJson) {
            return  JSONArray.parseArray(sqlJson);
        }
        return null;
    }

    /**
     * 根据列索引，获取可以为空的结果
     * @param resultSet
     * @param i
     * @return
     * @throws SQLException
     */
    @Override
    public JSONArray getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String sqlJson = resultSet.getString(i);
        if (null != sqlJson) {
            return JSONArray.parseArray(sqlJson);
        }
        return null;
    }

    @Override
    public JSONArray getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String sqlJson = callableStatement.getString(i);
        if (null != sqlJson) {
            return JSONArray.parseArray(sqlJson);
        }
        return null;
    }
}
