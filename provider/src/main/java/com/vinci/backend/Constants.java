package com.vinci.backend;

import com.vinci.common.base.exception.ErrorCode;
import com.vinci.common.base.exception.ErrorType;

/**
 * 用户操作的一些常量
 * Created by tim@vinci on 15-1-28.
 */
public class Constants {
    public final static String USER_DATABASE_NAME = "users";
    public final static String RELATION_DATABASE_NAME = "relations";
    public final static String FEED_DATABASE_NAME = "feed";

    public final static int MAX_FOLLOW_LENGTH_ONCE = 20;

    public static final ErrorCode ERROR_DATABASE_FAILED = new ErrorCode(ErrorType.databaseErrorType, 1, "数据库错误");
    public static final ErrorCode ERROR_UNKNOWN_ERROR = new ErrorCode(ErrorType.unknownErrorType, 1, "未知错误");

    public static final ErrorCode ERROR_ATTENTION_USERID_IS_NEGATIVE = new ErrorCode(ErrorType.ArgumentErrorType, 1, "关注人为空或不存在");
    public static final ErrorCode ERROR_FOLLOWER_ID_IS_NEGATIVE = new ErrorCode(ErrorType.ArgumentErrorType, 2, "被关注人id不存在");
    public static final ErrorCode ERROR_FOLLOWER_ID_LENGTH_IS_NULL = new ErrorCode(ErrorType.ArgumentErrorType, 3, "没有选择关注人");
    public static final ErrorCode ERROR_FOLLOWER_ID_LENGTH_TOO_MANY = new ErrorCode(ErrorType.ArgumentErrorType, 4, "要关注的人不能超过" + MAX_FOLLOW_LENGTH_ONCE);
    public static final ErrorCode ERROR_FEED_NULL = new ErrorCode(ErrorType.ArgumentErrorType, 5, "分享失败，数据为空");
    public static final ErrorCode ERROR_FEED_CONTENT_NULL = new ErrorCode(ErrorType.ArgumentErrorType, 6, "分享内容为空");

    public static final ErrorCode ERROR_DEVICE_IS_NEGATIVE = new ErrorCode(ErrorType.ArgumentErrorType, 10, "要查询的设备id为负值");
    public static final ErrorCode ERROR_IMEI_IS_EMPTY = new ErrorCode(ErrorType.ArgumentErrorType, 11, "IMEI号为空");
    public static final ErrorCode ERROR_MAC_ADDR_IS_EMPTY = new ErrorCode(ErrorType.ArgumentErrorType, 12, "MAC地址为空");
    public static final ErrorCode ERROR_DEVICE_ID_INVALID = new ErrorCode(ErrorType.ArgumentErrorType, 13, "要插入的设备号有误");
    public static final ErrorCode ERROR_USERID_IS_EMPTY = new ErrorCode(ErrorType.ArgumentErrorType, 14, "userID为空");
    public static final ErrorCode ERROR_USER_IS_NOT_EXIST = new ErrorCode(ErrorType.dataConventionErrorType, 16, "用户不存在");
    public static final ErrorCode ERROR_NICKNAME_IS_EMPTY = new ErrorCode(ErrorType.ArgumentErrorType, 17, "昵称为空");
    public static final ErrorCode ERROR_USER_SETTINGS_IS_EMPTY = new ErrorCode(ErrorType.ArgumentErrorType, 18, "要修改的用户设置为空");

    public static final ErrorCode ERROR_HAS_ATTENTION = new ErrorCode(ErrorType.dataConventionErrorType, 1, "已经关注过，不能重复关注");
    public static final ErrorCode ERROR_DEVICE_IS_NOT_EXIST = new ErrorCode(ErrorType.dataConventionErrorType, 2, "不存在这个设备号");
    public static final ErrorCode ERROR_DEVICE_HAS_EXIST = new ErrorCode(ErrorType.dataConventionErrorType, 3, "设备号已存在");
    public static final ErrorCode ERROR_NICKNAME_HAS_USED = new ErrorCode(ErrorType.dataConventionErrorType, 4, "昵称已经被使用");
    public static final ErrorCode ERROR_USER_SETTINGS_UPDATE_CONFLICT = new ErrorCode(ErrorType.dataConventionErrorType, 5, "用户设置已经被修改，有冲突");
    public static final ErrorCode ERROR_DEVICE_HAS_BIND = new ErrorCode(ErrorType.dataConventionErrorType, 6, "设备已经被绑定，请解绑后再试");
    public static final ErrorCode ERROR_DEVICE_IS_NOT_BIND = new ErrorCode(ErrorType.dataConventionErrorType, 7, "设备未绑定用户");
    public static final ErrorCode ERROR_DEVICE_IS_NOT_BIND_OF_THAT_USER = new ErrorCode(ErrorType.dataConventionErrorType, 8, "要设备和当前用户没有绑定");

}
