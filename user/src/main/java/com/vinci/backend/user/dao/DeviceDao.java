package com.vinci.backend.user.dao;

import com.sun.istack.internal.NotNull;
import com.vinci.backend.user.model.DeviceInfo;
import com.vinci.common.base.exception.BizException;
import com.vinci.common.base.exception.ErrorCode;
import com.vinci.common.base.exception.ModelType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.*;

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
            throw new BizException(new ErrorCode(ModelType.user,1,"要查询的设备id为负值"));
        }
        DeviceInfo info = jdbcTemplate.query("select id,imei,mac_addr,userid from users.device where id=?", new ResultSetExtractor<DeviceInfo>() {
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
        }, id);
        if (info == null || info.getId() != id) {
            throw new BizException(new ErrorCode(ModelType.user,2,"要查询的设备号不存在"));
        }
        return info;
    }

    public DeviceInfo insert(final DeviceInfo deviceInfo) {
        if (deviceInfo == null || deviceInfo.getId() != 0) {
            throw new BizException(new ErrorCode(ModelType.user,3,"要插入的设备号有误"));
        }
        if (StringUtils.isEmpty(deviceInfo.getImei())) {
            throw new BizException(new ErrorCode(ModelType.user,6,"要插入的设备号有误，IMEI号为空"));
        }
        if (StringUtils.isEmpty(deviceInfo.getMacAddr())) {
            throw new BizException(new ErrorCode(ModelType.user,7,"要插入的设备号有误，MAC地址为空"));
        }
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            int rowCount = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement statement = con.prepareStatement("insert into users.device (imei,mac_addr,userid) values (?,?,?)"
                            , Statement.RETURN_GENERATED_KEYS);
                    statement.setString(1, deviceInfo.getImei());
                    statement.setString(2, deviceInfo.getMacAddr());
                    if (deviceInfo.getUserId() == null) {
                        statement.setString(3, "");
                    } else {
                        statement.setString(3, deviceInfo.getUserId());
                    }
                    return statement;
                }
            }, keyHolder);
            if (rowCount == 1) {
                deviceInfo.setId(keyHolder.getKey().longValue());
                return deviceInfo;
            }
        } catch (DuplicateKeyException e) {
            throw new BizException(e,new ErrorCode(ModelType.user,5,"插入的设备号失败，设备号已存在"));
        } catch (DataAccessException e) {
            throw new BizException(e,new ErrorCode(ModelType.user,8,"插入的设备号失败，数据库错误"));
        }
        throw new BizException(new ErrorCode(ModelType.user,4,"插入的设备号失败，未知错误"));
    }
}
