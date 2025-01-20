package com.iqiongzhi.SCB.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public class UserSQLProvider {

    public String buildGetUserIdQuery(Map<String, Object> params) {
        String id = (String) params.get("id");
        String type = (String) params.get("type");

        // 构建动态 SQL
        StringBuilder sql = new StringBuilder("SELECT id FROM user WHERE ");

        // 根据 type 参数动态生成条件
        if ("wechat".equalsIgnoreCase(type)) {
            sql.append("wechat = #{id}");
        } else if ("SDUId".equalsIgnoreCase(type)) {
            sql.append("student_id = #{id}");
        } else if ("username".equalsIgnoreCase(type)) {
            sql.append("username = #{id}");
        } else if ("email".equalsIgnoreCase(type)) {
            sql.append("email = #{id}");
        } else {
            sql.append("(wechat = #{id} OR student_id = #{id} OR username = #{id})");
        }

        return sql.toString();
    }
}