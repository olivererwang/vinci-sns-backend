package com.vinci.backend.user.dao;

import com.vinci.backend.user.model.UserModel;
import com.vinci.common.base.exception.BizException;
import com.vinci.common.base.exception.ErrorCode;
import com.vinci.common.base.exception.ErrorType;
import com.vinci.common.base.exception.ModelType;
import com.vinci.common.web.util.JsonUtils;

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
 * 用户表（user）的内容操作
 * Created by tim@vinci on 15-1-29.
 */

@Component
public class UserDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public UserModel getUser(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.argumentErrorType, 21, "UserId为空"));
        }
        try {
            UserModel info = jdbcTemplate.query("SELECT id,userid,device_id,nick_name,version,extra,create_date,update_time FROM " + USER_DATABASE_NAME + ".user WHERE userid=?",
                    new ResultSetExtractor<UserModel>() {
                        @Override
                        public UserModel extractData(ResultSet rs) throws SQLException, DataAccessException {
                            if (rs == null) {
                                return null;
                            }
                            if (rs.next()) {
                                UserModel info = new UserModel();
                                info.setId(rs.getLong("id"));
                                info.setUserId(rs.getString("userid"));
                                info.setDeviceId(rs.getLong("device_id"));
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
        } catch (BizException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        } catch (Exception e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.unknowErrorType, 1, "未知错误"));
        }
    }

    public void modifyUserNickName(String userId, String nickName) {
        if (StringUtils.isEmpty(userId)) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.argumentErrorType, 21, "UserId为空"));
        }
        if (StringUtils.isEmpty(nickName)) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.argumentErrorType, 22, "昵称为空"));
        }
        try {
            int rowCount = jdbcTemplate.update("UPDATE " + USER_DATABASE_NAME + ".user SET nick_name=? WHERE userid=?",
                    nickName,userId);
            if (rowCount == 0) {
                throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 11, "用户不存在"));
            }
        } catch (BizException e) {
            throw e;
        } catch (DuplicateKeyException e) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 12, "昵称已经被使用"));
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        } catch (Exception e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.unknowErrorType, 1, "未知错误"));
        }
    }

    public void changeUserDevice(String userId, long deviceId) {
        if (StringUtils.isEmpty(userId)) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.argumentErrorType, 21, "UserId为空"));
        }
        try {
            int rowCount = jdbcTemplate.update("UPDATE " + USER_DATABASE_NAME + ".user SET device_id=? WHERE userid=?",
                    deviceId,userId);
            if (rowCount == 0) {
                throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 11, "用户不存在"));
            }
        } catch (BizException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        } catch (Exception e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.unknowErrorType, 1, "未知错误"));
        }
    }

    public void modifyUserSettings(String userId, UserModel.UserSettings userSettings , int version) {
        if (StringUtils.isEmpty(userId)) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.argumentErrorType, 21, "UserId为空"));
        }
        if (userSettings == null) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.argumentErrorType, 23, "要修改的用户设置为空"));
        }
        try {
            int rowCount = jdbcTemplate.update("UPDATE " + USER_DATABASE_NAME + ".user SET extra=?,version=version+1 WHERE userid=? and version=?",
                    userSettings.toString(),userId,version);
            if (rowCount == 0) {
                throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 13, "用户设置已经被修改，有冲突"));
            }
        } catch (BizException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        } catch (Exception e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.unknowErrorType, 1, "未知错误"));
        }
    }

    public UserModel newUser(final UserModel userModel) {
        if (userModel == null) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.argumentErrorType, 24, "要插入的用户数据为空"));
        }
        if (StringUtils.isEmpty(userModel.getUserId())) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.argumentErrorType, 21, "UserId为空"));
        }
        if (StringUtils.isEmpty(userModel.getNickName())) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.argumentErrorType, 22, "昵称为空"));
        }
        if (userModel.getUserSettings() == null) {
            userModel.setUserSettings(new UserModel.UserSettings());
        }
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            int rowCount = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement statement = con.prepareStatement("insert into " + USER_DATABASE_NAME + ".user (userid,nick_name,device_id,extra) values (?,?,?,?)"
                            , Statement.RETURN_GENERATED_KEYS);
                    statement.setString(1,userModel.getUserId());
                    statement.setString(2,userModel.getNickName());
                    statement.setLong(3,userModel.getDeviceId());
                    statement.setString(4,userModel.getUserSettings().toString());
                    return statement;
                }
            },keyHolder);
            if (rowCount == 1) {
                userModel.setId(keyHolder.getKey().longValue());
                return userModel;
            }
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.unknowErrorType, 1, "未知错误"));
        } catch (BizException e) {
            throw e;
        } catch (DuplicateKeyException e) {
            throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 12, "昵称已经被使用"));
        } catch (DataAccessException e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.databaseErrorType, 1, "数据库错误"));
        } catch (Exception e) {
            throw new BizException(e, new ErrorCode(ModelType.user, ErrorType.unknowErrorType, 1, "未知错误"));
        }
    }
}
