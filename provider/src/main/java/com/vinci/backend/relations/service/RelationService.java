package com.vinci.backend.relations.service;

import com.google.common.collect.Lists;
import com.vinci.backend.relations.dao.RelationDao;
import com.vinci.backend.relations.model.Attention;
import com.vinci.backend.user.model.UserModel;
import com.vinci.backend.user.service.UserService;
import com.vinci.backend.util.BizTemplate;
import com.vinci.common.base.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static com.vinci.backend.Constants.*;

/**
 * 关注关系的service
 * Created by tim@vinci on 15-1-30.
 */
@Service
public class RelationService {

    @Resource
    private UserService userService;
    @Resource
    private RelationDao relationDao;


    /**
     * 创建
     */
    public List<UserModel> createAttention(final UserModel source , final List<Long> dstUserIds) {
        return new BizTemplate<List<UserModel>>("createAttention") {
            @Override
            protected void checkParams() throws BizException {
                if (source == null) {
                    throw new BizException(ERROR_USERID_IS_NEGATIVE);
                }
                if (dstUserIds == null || dstUserIds.size() == 0) {
                    throw new BizException(ERROR_FOLLOWER_ID_LENGTH_IS_NULL);
                }
                if (dstUserIds.size() > MAX_FOLLOW_LENGTH_ONCE) {
                    throw new BizException(ERROR_FOLLOWER_ID_LENGTH_TOO_MANY);
                }
            }

            @Override
            protected List<UserModel> process() throws Exception {
                List<UserModel> users = userService.getUserByUserID(dstUserIds);
                if (users == null || users.size() == 0) {
                    throw new BizException(ERROR_USERID_IS_NEGATIVE);
                }
                List<Long> dstUserIds = Lists.newArrayListWithCapacity(users.size());
                for (UserModel user : users) {
                    if (user != null) {
                        dstUserIds.add(user.getId());
                    }
                }
                relationDao.createRelation(source.getId(),dstUserIds);
                return users;
            }
        }.execute();
    }


    /**
     * 获取关注或粉丝关系
     */
    public List<Attention> getAttention(final UserModel source , final long lastId , final int size , final boolean isAttention) {
        return new BizTemplate<List<Attention>>("getAttention") {
            @Override
            protected void checkParams() throws BizException {
                if (source == null) {
                    throw new BizException(ERROR_USERID_IS_NEGATIVE);
                }
            }

            @Override
            protected List<Attention> process() throws Exception {
                List<Attention> attention = relationDao.getAttentions(source.getId(), lastId, size, isAttention);
                if (attention == null) {
                    return Collections.emptyList();
                }
                return attention;
            }
        }.execute();
    }

    public boolean isAttention(final UserModel source , final long dstId) {
        return new BizTemplate<Boolean>("isAttention") {
            @Override
            protected void checkParams() throws BizException {
                if (source == null) {
                    throw new BizException(ERROR_USERID_IS_NEGATIVE);
                }
                if (dstId <= 0) {
                    throw new BizException(ERROR_FOLLOWER_ID_LENGTH_IS_NULL);
                }
            }

            @Override
            protected Boolean process() throws Exception {
                return relationDao.isAttention(source.getId(),dstId);
            }
        }.execute();
    }
}
