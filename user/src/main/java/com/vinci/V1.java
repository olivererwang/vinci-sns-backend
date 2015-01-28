package com.vinci;

import com.vinci.common.base.api.APIResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by tim@vinci on 15-1-27.
 */
@Controller
public class V1 {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "test" , method = RequestMethod.GET)
    @ResponseBody
    public APIResponse<List<String>> test() {
        String sql = "show databases";

        List<String> result = jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
        return APIResponse.returnSuccess(result);
    }

}
