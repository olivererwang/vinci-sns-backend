package com.vinci.backend;

import com.vinci.common.base.exception.ErrorCode;
import com.vinci.common.base.exception.ErrorType;
import com.vinci.common.base.exception.ModelType;

/**
 * 用户操作的一些常量
 * Created by tim@vinci on 15-1-28.
 */
public class Constants {
    public final static String USER_DATABASE_NAME = "users";
    public final static String RELATION_DATABASE_NAME = "relations";


    public final static int MAX_FOLLOW_LENGTH_ONCE = 20;

    public static final ErrorCode RELATION_ERROR_DATABASE_FAILED = new ErrorCode(ModelType.relations, ErrorType.databaseErrorType, 1, "数据库错误");
    public static final ErrorCode RELATION_ERROR_USERID_IS_NEGATIVE = new ErrorCode(ModelType.relations, ErrorType.ArgumentErrorType, 1, "关注人为空或不存在");
    public static final ErrorCode RELATION_ERROR_FOLLOWER_ID_IS_NEGATIVE = new ErrorCode(ModelType.relations, ErrorType.ArgumentErrorType, 2, "被关注人id不存在");
    public static final ErrorCode RELATION_ERROR_FOLLOWER_ID_LENGTH_IS_NULL = new ErrorCode(ModelType.relations, ErrorType.ArgumentErrorType, 3, "没有选择关注人");
    public static final ErrorCode RELATION_ERROR_FOLLOWER_ID_LENGTH_TOO_MANY = new ErrorCode(ModelType.relations, ErrorType.ArgumentErrorType, 4, "要关注的人不能超过"+MAX_FOLLOW_LENGTH_ONCE);
    public static final ErrorCode RELATION_ERROR_HAS_ATTENTION = new ErrorCode(ModelType.relations, ErrorType.dataConventionErrorType, 1, "已经关注过，不能重复关注");

}
