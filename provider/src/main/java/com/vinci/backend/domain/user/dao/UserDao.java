package com.vinci.backend.domain.user.dao;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vinci.backend.domain.user.model.UserModel;
import com.vinci.common.base.exception.BizException;
import com.vinci.common.web.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.vinci.backend.domain.Constants.*;

/**
 * 用户表（user）的内容操作
 * Created by tim@vinci on 15-1-29.
 */

@Repository
public class UserDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public UserModel getUser(long userId) {
        try {
            UserModel info = jdbcTemplate.query("SELECT id,device_imei,nick_name,version,extra,create_date,update_time FROM " + USER_DATABASE_NAME + ".user WHERE id=?",
                    new ResultSetExtractor<UserModel>() {
                        @Override
                        public UserModel extractData(ResultSet rs) throws SQLException, DataAccessException {
                            if (rs == null) {
                                return null;
                            }
                            if (rs.next()) {
                                UserModel info = new UserModel();
                                info.setId(rs.getLong("id"));
                                info.setDeviceIMEI(rs.getString("device_imei"));
                                info.setNickName(rs.getString("nick_name"));
                                info.setVersion(rs.getInt("version"));
                                info.setUserSettings(JsonUtils.decode(rs.getString("extra"), UserModel.UserSettings.class));
                                info.setCreateDate(rs.getTimestamp("create_date"));
                                info.setUpdateTime(rs.getTimestamp("update_time"));
                                return info;
                            }
                            return null;
                        }
                    }, userId);
            return info;
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }
    }

    public Map<Long, UserModel> getUser(final List<Long> users) {
        if (users == null || users.size() == 0) {
            return Collections.emptyMap();
        }
        try {
            Map<Long, UserModel> info = jdbcTemplate.query("SELECT id,device_imei,nick_name,version,extra,create_date,update_time FROM "
                            + USER_DATABASE_NAME + ".user WHERE id in (" + Joiner.on(',').skipNulls().join(users) + ")",
                    new ResultSetExtractor<Map<Long, UserModel>>() {
                        @Override
                        public Map<Long, UserModel> extractData(ResultSet rs) throws SQLException, DataAccessException {
                            if (rs == null) {
                                return Collections.emptyMap();
                            }
                            Map<Long, UserModel> result = Maps.newHashMapWithExpectedSize(users.size());
                            while (rs.next()) {
                                UserModel info = new UserModel();
                                info.setId(rs.getLong("id"));
                                info.setDeviceIMEI(rs.getString("device_imei"));
                                info.setNickName(rs.getString("nick_name"));
                                info.setVersion(rs.getInt("version"));
                                info.setUserSettings(JsonUtils.decode(rs.getString("extra"), UserModel.UserSettings.class));
                                info.setCreateDate(rs.getTimestamp("create_date"));
                                info.setUpdateTime(rs.getTimestamp("update_time"));
                                result.put(info.getId(), info);
                            }
                            return result;
                        }
                    });
            if (info == null) {
                return Collections.emptyMap();
            }
            return info;
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }
    }

    public void modifyUserNickName(long userId, String nickName) {
        if (StringUtils.isEmpty(nickName)) {
            throw new BizException(ERROR_NICKNAME_IS_EMPTY);
        }
        try {
            int rowCount = jdbcTemplate.update("UPDATE " + USER_DATABASE_NAME + ".user SET nick_name=? WHERE id=?",
                    nickName,userId);
            if (rowCount == 0) {
                throw new BizException(ERROR_USER_IS_NOT_EXIST);
            }
        } catch (DuplicateKeyException e) {
            throw new BizException(ERROR_NICKNAME_HAS_USED);
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }
    }

    public void changeUserDevice(long userId, String deviceIMEI) {
        try {
            int rowCount = jdbcTemplate.update("UPDATE " + USER_DATABASE_NAME + ".user SET device_imei=? WHERE id=?",
                    deviceIMEI,userId);
            if (rowCount == 0) {
                throw new BizException(ERROR_USER_IS_NOT_EXIST);
            }
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }
    }

    public void modifyUserSettings(long userId, UserModel.UserSettings userSettings , int version) {
        if (userSettings == null) {
            throw new BizException(ERROR_USER_SETTINGS_IS_EMPTY);
        }
        try {
            int rowCount = jdbcTemplate.update("UPDATE " + USER_DATABASE_NAME + ".user SET extra=?,version=version+1 WHERE id=? AND version=?",
                    userSettings.toString(),userId,version);
            if (rowCount == 0) {
                throw new BizException(ERROR_USER_SETTINGS_UPDATE_CONFLICT);
            }
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }
    }

    public long newUser(final UserModel userModel) {
        if (userModel == null) {
            throw new BizException(ERROR_USER_SETTINGS_IS_EMPTY);
        }
        if (StringUtils.isEmpty(userModel.getNickName())) {
            throw new BizException(ERROR_NICKNAME_IS_EMPTY);
        }
        if (userModel.getUserSettings() == null) {
            userModel.setUserSettings(new UserModel.UserSettings());
        }
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            int rowCount = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement statement = con.prepareStatement("insert into " + USER_DATABASE_NAME + ".user (nick_name,device_imei,extra) values (?,?,?)",
                            Statement.RETURN_GENERATED_KEYS);
                    statement.setString(1,userModel.getNickName());
                    statement.setString(2,userModel.getDeviceIMEI()==null?"":userModel.getDeviceIMEI());
                    statement.setString(3,userModel.getUserSettings().toString());
                    return statement;
                }
            },keyHolder);
            if (rowCount != 1) {
                throw new BizException(ERROR_UNKNOWN_ERROR);
            }
            return keyHolder.getKey().longValue();
        } catch (DuplicateKeyException e) {
            throw new BizException(ERROR_NICKNAME_HAS_USED);
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }
    }
}
