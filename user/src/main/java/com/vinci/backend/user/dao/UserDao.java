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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            UserModel info = jdbcTemplate.query("SELECT id,userid,device_id,version,extra,create_date,update_time FROM "+USER_DATABASE_NAME+".user WHERE userid=?",
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
                                info.setVersion(rs.getInt("version"));
                                info.setUserSettings(JsonUtils.decode(rs.getString("extra"), UserModel.UserSettings.class));
                                info.setCreateDate(rs.getTimestamp("create_date"));
                                info.setUpdateTime(rs.getTimestamp("update_time"));
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
}
