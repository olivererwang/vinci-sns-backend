package com.vinci.backend.user.dao;

import com.vinci.backend.user.model.DeviceInfo;
import com.vinci.common.base.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.vinci.backend.Constants.*;

/**
 * 对Device表进行的基本操作
 * Created by tim@vinci on 15-1-28.
 */
@Repository
public class DeviceDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public DeviceInfo getDeviceInfoById(long id) {
        if (id <= 0) {
            throw new BizException(ERROR_DEVICE_IS_NEGATIVE);
        }
        try {
            DeviceInfo info = jdbcTemplate.query("SELECT id,imei,mac_addr,userid,create_date,update_time FROM "+USER_DATABASE_NAME+".device WHERE id=?", new ResultSetExtractor<DeviceInfo>() {
                @Override
                public DeviceInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs == null) {
                        return null;
                    }
                    if (rs.next()) {
                        DeviceInfo info = new DeviceInfo();
                        info.setId(rs.getLong("id"));
                        info.setImei(rs.getString("imei"));
                        info.setMacAddr(rs.getString("mac_addr"));
                        info.setUserId(rs.getLong("userid"));
                        info.setCreateDate(rs.getTimestamp("create_date"));
                        info.setUpdateTime(rs.getTimestamp("update_time"));
                        return info;
                    }
                    return null;
                }
            }, id);
            if (info == null || info.getId() != id) {
                throw new BizException(ERROR_DEVICE_IS_NOT_EXIST);
            }
            return info;
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }
    }


    public DeviceInfo getDeviceInfo(String imei, String macAddr) {
        if (StringUtils.isEmpty(imei)) {
            throw new BizException(ERROR_IMEI_IS_EMPTY);
        }
        if (StringUtils.isEmpty(macAddr)) {
            throw new BizException(ERROR_MAC_ADDR_IS_EMPTY);
        }
        try {
            DeviceInfo info = jdbcTemplate.query("SELECT id,imei,mac_addr,userid,create_date,update_time FROM "+USER_DATABASE_NAME+".device WHERE imei=? AND mac_addr=?",
                    new ResultSetExtractor<DeviceInfo>() {
                        @Override
                        public DeviceInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
                            if (rs == null) {
                                return null;
                            }
                            if (rs.next()) {
                                DeviceInfo info = new DeviceInfo();
                                info.setId(rs.getLong("id"));
                                info.setImei(rs.getString("imei"));
                                info.setMacAddr(rs.getString("mac_addr"));
                                info.setUserId(rs.getLong("userid"));
                                info.setCreateDate(rs.getTimestamp("create_date"));
                                info.setUpdateTime(rs.getTimestamp("update_time"));
                                return info;
                            }
                            return null;
                        }
                    }, imei, macAddr);
            if (info == null) {
                throw new BizException(ERROR_DEVICE_IS_NOT_EXIST);
            }
            return info;
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }
    }

    public DeviceInfo getDeviceInfoByBindUser(final long userID) {
        try {
            DeviceInfo info = jdbcTemplate.query("SELECT id,imei,mac_addr,userid,create_date,update_time FROM "+USER_DATABASE_NAME+".device WHERE userid=?",
                    new ResultSetExtractor<DeviceInfo>() {
                        @Override
                        public DeviceInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
                            if (rs == null) {
                                return null;
                            }
                            if (rs.next()) {
                                DeviceInfo info = new DeviceInfo();
                                info.setId(rs.getLong("id"));
                                info.setImei(rs.getString("imei"));
                                info.setMacAddr(rs.getString("mac_addr"));
                                info.setUserId(rs.getLong("userid"));
                                info.setCreateDate(rs.getTimestamp("create_date"));
                                info.setUpdateTime(rs.getTimestamp("update_time"));
                                return info;
                            }
                            return null;
                        }
                    }, userID );
            return info;
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }
    }

    public void insert(final DeviceInfo deviceInfo) {
        if (deviceInfo == null || deviceInfo.getId() != 0) {
            throw new BizException(ERROR_DEVICE_ID_INVALID);
        }
        if (StringUtils.isEmpty(deviceInfo.getImei())) {
            throw new BizException(ERROR_IMEI_IS_EMPTY);
        }
        if (StringUtils.isEmpty(deviceInfo.getMacAddr())) {
            throw new BizException(ERROR_MAC_ADDR_IS_EMPTY);
        }
        try {
            int rowCount = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement statement = con.prepareStatement("INSERT INTO "+USER_DATABASE_NAME+".device (imei,mac_addr,userid) VALUES (?,?,?)");
                    statement.setString(1, deviceInfo.getImei());
                    statement.setString(2, deviceInfo.getMacAddr());
                    statement.setLong(3, deviceInfo.getUserId());
                    return statement;
                }
            });
            if (rowCount != 1) {
                throw new BizException(ERROR_UNKNOWN_ERROR);
            }
        } catch (DuplicateKeyException e) {
            throw new BizException(e, ERROR_DEVICE_HAS_EXIST);
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }

    }

    public void updateBindUser(final DeviceInfo deviceInfo) {
        if (deviceInfo == null || deviceInfo.getId() == 0) {
            throw new BizException(ERROR_DEVICE_ID_INVALID);
        }
        if (StringUtils.isEmpty(deviceInfo.getImei())) {
            throw new BizException(ERROR_IMEI_IS_EMPTY);
        }
        if (StringUtils.isEmpty(deviceInfo.getMacAddr())) {
            throw new BizException(ERROR_MAC_ADDR_IS_EMPTY);
        }
        if (deviceInfo.getUserId() <= 0) {
            throw new BizException(ERROR_USERID_IS_EMPTY);
        }
        try {
            int rowCount = jdbcTemplate.update("UPDATE "+USER_DATABASE_NAME+".device SET userid=? WHERE id=? AND imei=? AND mac_addr=?",
                    deviceInfo.getUserId(), deviceInfo.getId(), deviceInfo.getImei(), deviceInfo.getMacAddr());
            if (rowCount == 0) {
                throw new BizException(ERROR_DEVICE_IS_NOT_EXIST);
            }
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }
    }
}
