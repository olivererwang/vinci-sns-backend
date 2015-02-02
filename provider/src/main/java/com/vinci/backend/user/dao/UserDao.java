package com.vinci.backend.user.dao;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.vinci.backend.user.model.UserModel;
import com.vinci.common.base.exception.BizException;
import com.vinci.common.base.exception.ErrorCode;
import com.vinci.common.base.exception.ErrorType;
import com.vinci.common.base.exception.ModelType;
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

import static com.vinci.backend.Constants.USER_DATABASE_NAME;

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
            UserModel info = jdbcTemplate.query("SELECT id,userid,device_imei,nick_name,version,extra,create_date,update_time FROM " + USER_DATABASE_NAME + ".user WHERE userid=?",
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
            if (info == null) {
                throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 11, "用户不存在"));
            }
            return info;
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        }
    }

    public List<UserModel> getUser(final List<Long> users) {
        if (users == null || users.size() == 0) {
            return Collections.emptyList();
        }
        try {
            List<UserModel> info = jdbcTemplate.query("SELECT id,userid,device_imei,nick_name,version,extra,create_date,update_time FROM "
                            + USER_DATABASE_NAME + ".user WHERE userid in ("+ Joiner.on(',').skipNulls().join(users)+")",
                    new ResultSetExtractor<List<UserModel>>() {
                        @Override
                        public List<UserModel> extractData(ResultSet rs) throws SQLException, DataAccessException {
                            if (rs == null) {
                                return Collections.emptyList();
                            }
                            List<UserModel> list = Lists.newArrayListWithCapacity(users.size());
                            while (rs.next()) {
                                UserModel info = new UserModel();
                                info.setId(rs.getLong("id"));
                                info.setDeviceIMEI(rs.getString("device_imei"));
                                info.setNickName(rs.getString("nick_name"));
                                info.setVersion(rs.getInt("version"));
                                info.setUserSettings(JsonUtils.decode(rs.getString("extra"), UserModel.UserSettings.class));
                                info.setCreateDate(rs.getTimestamp("create_date"));
                                info.setUpdateTime(rs.getTimestamp("update_time"));
                                list.add(info);
                            }
                            return list;
                        }
                    });
            if (info == null) {
                return Collections.emptyList();
            }
            return info;
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        }
    }

    public void modifyUserNickName(long userId, String nickName) {
        if (StringUtils.isEmpty(nickName)) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 22, "昵称为空"));
        }
        try {
            int rowCount = jdbcTemplate.update("UPDATE " + USER_DATABASE_NAME + ".user SET nick_name=? WHERE userid=?",
                    nickName,userId);
            if (rowCount == 0) {
                throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 11, "用户不存在"));
            }
        } catch (DuplicateKeyException e) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 12, "昵称已经被使用"));
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        }
    }

    public void changeUserDevice(long userId, String deviceIMEI) {
        try {
            int rowCount = jdbcTemplate.update("UPDATE " + USER_DATABASE_NAME + ".user SET device_imei=? WHERE userid=?",
                    deviceIMEI,userId);
            if (rowCount == 0) {
                throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 11, "用户不存在"));
            }
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        }
    }

    public void modifyUserSettings(long userId, UserModel.UserSettings userSettings , int version) {
        if (userSettings == null) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 23, "要修改的用户设置为空"));
        }
        try {
            int rowCount = jdbcTemplate.update("UPDATE " + USER_DATABASE_NAME + ".user SET extra=?,version=version+1 WHERE userid=? and version=?",
                    userSettings.toString(),userId,version);
            if (rowCount == 0) {
                throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 13, "用户设置已经被修改，有冲突"));
            }
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        }
    }

    public long newUser(final UserModel userModel) {
        if (userModel == null) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 24, "要插入的用户数据为空"));
        }
        if (StringUtils.isEmpty(userModel.getNickName())) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 22, "昵称为空"));
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
                throw new BizException(new ErrorCode(ModelType.user, ErrorType.unknowErrorType, 1, "未知错误"));
            }
            return keyHolder.getKey().longValue();
        } catch (DuplicateKeyException e) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 12, "昵称已经被使用"));
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        }
    }
}
