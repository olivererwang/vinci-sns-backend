package com.vinci.backend.feed.service;

import com.vinci.backend.feed.dao.FeedDao;
import com.vinci.backend.feed.dao.TimelineDao;
import com.vinci.backend.feed.model.FeedModel;
import com.vinci.backend.relations.service.RelationService;
import com.vinci.backend.user.model.UserModel;
import com.vinci.backend.user.service.UserService;
import com.vinci.backend.util.BizTemplate;
import com.vinci.common.base.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 分享内容相关的操作
 * Created by tim@vinci on 15-2-2.
 */
@Service
public class FeedService {

    @Resource
    private UserService userService;

    @Resource
    private RelationService relationService;

    @Resource
    private FeedDao feedDao;

    @Resource
    private TimelineDao timelineDao;

    /**
     * 发分享
     */
    public FeedModel insertFeed(UserModel user , FeedModel feed) {
        return new BizTemplate<FeedModel>("insertFeed") {

            @Override
            protected void checkParams() throws BizException {

            }

            @Override
            protected FeedModel process() throws Exception {
                return null;
            }
        }.execute();
    }

    /**
     * 查看某人的所有分享
     */

    /**
     * 查看feed
     */
}
