package com.vinci;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by tim@vinci on 15-1-27.
 */
@Service
public class V1 {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public void test() {
        String sql = "show databases";

        List<String> result = jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
        System.out.println(result);
    }
}
