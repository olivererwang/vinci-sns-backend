package com.vinci.backend.user.dao;

import com.vinci.backend.user.model.DeviceInfo;
import com.vinci.common.base.exception.BizException;
import com.vinci.common.base.exception.ErrorCode;
import com.vinci.common.base.exception.ErrorType;
import com.vinci.common.base.exception.ModelType;
import static com.vinci.backend.user.Constants.*;
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
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 1, "要查询的设备id为负值"));
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
                        info.setUserId(rs.getString("userid"));
                        info.setCreateDate(rs.getTimestamp("create_date"));
                        info.setUpdateTime(rs.getTimestamp("update_time"));
                        return info;
                    }
                    return null;
                }
            }, id);
            if (info == null || info.getId() != id) {
                throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 1, "不存在这个设备号"));
            }
            return info;
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        }
    }


    public DeviceInfo getDeviceInfo(String imei, String macAddr) {
        if (StringUtils.isEmpty(imei)) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 2, "IMEI号为空"));
        }
        if (StringUtils.isEmpty(macAddr)) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 3, "MAC地址为空"));
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
                                info.setUserId(rs.getString("userid"));
                                info.setCreateDate(rs.getTimestamp("create_date"));
                                info.setUpdateTime(rs.getTimestamp("update_time"));
                                return info;
                            }
                            return null;
                        }
                    }, imei, macAddr);
            if (info == null) {
                throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 1, "不存在这个设备号"));
            }
            return info;
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        }
    }

    public DeviceInfo getDeviceInfoByBindUser(final String userID) {
        if (StringUtils.isEmpty(userID)) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 21, "userId为空"));
        }
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
                                info.setUserId(rs.getString("userid"));
                                info.setCreateDate(rs.getTimestamp("create_date"));
                                info.setUpdateTime(rs.getTimestamp("update_time"));
                                return info;
                            }
                            return null;
                        }
                    }, userID );
            return info;
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        }
    }

    public void insert(final DeviceInfo deviceInfo) {
        if (deviceInfo == null || deviceInfo.getId() != 0) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 4, "要插入的设备号有误"));
        }
        if (StringUtils.isEmpty(deviceInfo.getImei())) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 5, "要插入的设备号有误，IMEI号为空"));
        }
        if (StringUtils.isEmpty(deviceInfo.getMacAddr())) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 6, "要插入的设备号有误，MAC地址为空"));
        }
        try {
            int rowCount = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement statement = con.prepareStatement("INSERT INTO "+USER_DATABASE_NAME+".device (imei,mac_addr,userid) VALUES (?,?,?)");
                    statement.setString(1, deviceInfo.getImei());
                    statement.setString(2, deviceInfo.getMacAddr());
                    if (deviceInfo.getUserId() == null) {
                        statement.setString(3, "");
                    } else {
                        statement.setString(3, deviceInfo.getUserId());
                    }
                    return statement;
                }
            });
            if (rowCount != 1) {
                throw new BizException(new ErrorCode(ModelType.user, ErrorType.unknowErrorType, 1, "未知错误"));
            }
        } catch (DuplicateKeyException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 2, "插入的设备号失败，设备号已存在"));
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        }

    }

    public void updateBindUser(final DeviceInfo deviceInfo) {
        if (deviceInfo == null || deviceInfo.getId() == 0) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 7, "绑定设备参数为空"));
        }
        if (StringUtils.isEmpty(deviceInfo.getImei())) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 8, "绑定设备参数有误，IMEI号为空"));
        }
        if (StringUtils.isEmpty(deviceInfo.getMacAddr())) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 9, "绑定设备参数有误，MAC地址为空"));
        }
        if (deviceInfo.getUserId() == null) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 10, "绑定设备参数有误，userID为空"));
        }
        try {
            int rowCount = jdbcTemplate.update("UPDATE "+USER_DATABASE_NAME+".device SET userid=? WHERE id=? AND imei=? AND mac_addr=?",
                    deviceInfo.getUserId(), deviceInfo.getId(), deviceInfo.getImei(), deviceInfo.getMacAddr());
            if (rowCount == 0) {
                throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 1, "不存在这个设备号"));
            }
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        }
    }
}
