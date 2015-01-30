package com.vinci.backend.user.service;

import com.vinci.backend.user.dao.DeviceDao;
import com.vinci.backend.user.dao.UserDao;
import com.vinci.backend.user.model.DeviceInfo;
import com.vinci.backend.user.model.UserModel;
import com.vinci.backend.util.BizTemplate;
import com.vinci.common.base.exception.BizException;
import com.vinci.common.base.exception.ErrorCode;
import com.vinci.common.base.exception.ErrorType;
import com.vinci.common.base.exception.ModelType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by tim@vinci on 15-1-30.
 */
@Service
public class UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private DeviceDao deviceDao;

    @Resource
    private UserService thisService;
    /**
     * 新建用户
     */
    public UserModel createUser(final String nickname, final UserModel.UserSettings userSettings) {
        return new BizTemplate<UserModel>(ModelType.user, "createUser") {

            @Override
            protected void checkParams() throws BizException {
                if (StringUtils.isEmpty(nickname)) {
                    throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 22, "昵称为空"));
                }
            }

            @Override
            protected UserModel process() throws Exception {
                UserModel model = new UserModel();
                model.setNickName(nickname.trim());
                model.setUserSettings(userSettings);
                model.setUserId(UUID.randomUUID().toString());
                userDao.newUser(model);
                return userDao.getUser(model.getUserId());
            }
        }.execute();
    }


    /**
     * 通过userID获取用户资料
     */
    public UserModel getUserByUserID(final String userId) {
        return new BizTemplate<UserModel>(ModelType.user, "getUserByUserID") {

            @Override
            protected void checkParams() throws BizException {
                //do nothing
            }

            @Override
            protected UserModel process() throws Exception {
                return userDao.getUser(userId);
            }
        }.execute();
    }

    /**
     * 通过设备号获取绑定用户
     */
    public UserModel getUserByIMEI(final String IMEI, final String macAddr) {
        return new BizTemplate<UserModel>(ModelType.user, "getUserByDeviceID") {

            @Override
            protected void checkParams() throws BizException {
                //do nothing
            }

            @Override
            protected UserModel process() throws Exception {
                DeviceInfo deviceInfo = deviceDao.getDeviceInfo(IMEI, macAddr);
                if (deviceInfo == null) {
                    throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 1, "不存在这个设备号"));
                }
                if (StringUtils.isEmpty(deviceInfo.getUserId())) {
                    throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 21, "设备未绑定用户"));
                }
                return userDao.getUser(deviceInfo.getUserId());
            }
        }.execute();
    }


    /**
     * 绑定一个设备
     */
    public boolean bindDevice(final String userID, final String IMEI, final String macAddr) {
        return new BizTemplate<Boolean>(ModelType.user, "bindDevice") {

            @Override
            protected void checkParams() throws BizException {
                //do nothing
            }

            @Override
            protected Boolean process() throws Exception {
                DeviceInfo deviceInfo = deviceDao.getDeviceInfo(IMEI, macAddr);
                if (deviceInfo == null) {
                    throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 1, "不存在这个设备号"));
                }
                if (deviceInfo.getUserId() != null && deviceInfo.getUserId().equals(userID)) {
                    return Boolean.TRUE;
                }
                if (deviceDao.getDeviceInfoByBindUser(userID) != null) {
                    throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 22, "设备已经被绑定，请解绑后再试"));
                }
                UserModel userModel = userDao.getUser(userID);
                if (userModel == null) {
                    throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 11, "用户不存在"));
                }
                deviceInfo.setUserId(userModel.getUserId());
                userModel.setDeviceIMEI(deviceInfo.getImei());
                thisService.changeBindDevice(deviceInfo, userModel);
                return true;
            }
        }.execute();
    }

    /**
     * 解绑一个设备
     */
    public boolean unbindDevice(final String userID, final String IMEI, final String macAddr) {
        return new BizTemplate<Boolean>(ModelType.user, "unbindDevice") {

            @Override
            protected void checkParams() throws BizException {
                //do nothing
            }

            @Override
            protected Boolean process() throws Exception {
                DeviceInfo deviceInfo = deviceDao.getDeviceInfo(IMEI, macAddr);
                if (deviceInfo == null) {
                    throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 1, "不存在这个设备号"));
                }
                if (deviceInfo.getUserId() == null || !deviceInfo.getUserId().equals(userID)) {
                    throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 1, "要设备和当前用户没有绑定"));
                }
                UserModel userModel = userDao.getUser(userID);
                if (userModel == null) {
                    throw new BizException(new ErrorCode(ModelType.user, ErrorType.dataConventionErrorType, 11, "用户不存在"));
                }
                deviceInfo.setUserId("");
                userModel.setDeviceIMEI("");
                thisService.changeBindDevice(deviceInfo,userModel);
                return true;
            }
        }.execute();
    }

    public void updateUserSettings(final UserModel userModel) {
        new BizTemplate<Boolean>(ModelType.user, "unbindDevice") {

            @Override
            protected void checkParams() throws BizException {
                if (userModel == null) {
                    throw new BizException(new ErrorCode(ModelType.user, ErrorType.ArgumentErrorType, 23, "要修改的用户设置为空"));
                }
            }

            @Override
            protected Boolean process() throws Exception {
                userDao.modifyUserSettings(userModel.getUserId(),userModel.getUserSettings(),userModel.getVersion());
                return Boolean.TRUE;
            }
        }.execute();
    }

    @Transactional
    private void changeBindDevice(DeviceInfo deviceInfo , UserModel userModel) {
        deviceDao.updateBindUser(deviceInfo);
        userDao.changeUserDevice(userModel.getUserId(),deviceInfo.getImei());
    }
}
