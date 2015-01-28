package com.vinci.backend.user.dao;

import com.vinci.backend.user.model.DeviceInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 对Device表进行的基本操作
 * Created by tim@vinci on 15-1-28.
 */
@Component
public class DeviceDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public DeviceInfo getDeviceInfoById(long id) {
        if (id <= 0) {
            return null;
        }
        return jdbcTemplate.query("select id,imei,mac_addr,userid from users.device where id=?", new ResultSetExtractor<DeviceInfo>() {
            @Override
            public DeviceInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs == null || rs.getFetchSize() == 0) {
                    return null;
                }
                DeviceInfo info = new DeviceInfo();
                info.setId(rs.getLong("id"));
                info.setImei(rs.getString("imei"));
                info.setMacAddr(rs.getString("mac_addr"));
                info.setUserId(rs.getString("userid"));
                return info;
            }
        },id);
    }

    public DeviceInfo insert(DeviceInfo deviceInfo) {
        if (deviceInfo == null) {
            return null;
        }
        return null;
    }
}
