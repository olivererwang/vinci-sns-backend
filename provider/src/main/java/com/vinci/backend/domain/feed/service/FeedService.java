package com.vinci.backend.domain.feed.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vinci.backend.domain.feed.dao.FeedDao;
import com.vinci.backend.domain.feed.dao.TimelineDao;
import com.vinci.backend.domain.feed.model.FeedModel;
import com.vinci.backend.domain.feed.model.TimelineModel;
import com.vinci.backend.domain.relations.model.Attention;
import com.vinci.backend.domain.relations.service.RelationService;
import com.vinci.backend.domain.user.model.UserModel;
import com.vinci.backend.domain.BizTemplate;
import com.vinci.common.base.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static com.vinci.backend.domain.Constants.ERROR_FEED_CONTENT_NULL;
import static com.vinci.backend.domain.Constants.ERROR_USERID_IS_EMPTY;
/**
 * 分享内容相关的操作
 * Created by tim@vinci on 15-2-2.
 */
@Service
public class FeedService {

    @Resource
    private RelationService relationService;

    @Resource
    private FeedDao feedDao;

    @Resource
    private TimelineDao timelineDao;

    /**
     * 发分享
     */
    public FeedModel insertFeed(final UserModel user, final FeedModel feed) {
        return new BizTemplate<FeedModel>("insertFeed") {

            @Override
            protected void checkParams() throws BizException {
                if (user == null || user.getId() <= 0) {
                    throw new BizException(ERROR_USERID_IS_EMPTY);
                }
                if (feed == null || StringUtils.isEmpty(feed.getFeedType()) || StringUtils.isEmpty(feed.getContent())) {
                    throw new BizException(ERROR_FEED_CONTENT_NULL);
                }
            }

            @Override
            protected FeedModel process() throws Exception {
                feed.setUserid(user.getId());
                FeedModel newFeed = feedDao.insertFeed(feed);
                List<Attention> followers = relationService.getAttention(user, 0, 0, false);
                if (followers == null || followers.size() == 0) {
                    return feedDao.getFeedById(Sets.newHashSet(newFeed.getId())).get(newFeed.getId());
                }
                List<Long> followerIds = Lists.newArrayListWithCapacity(followers.size());
                for (Attention attention : followers) {
                    if (attention != null && attention.getSourceUserId() > 0) {
                        followerIds.add(attention.getSourceUserId());
                    }
                }
                timelineDao.insertSomeonesTimeline(newFeed, followerIds);
                return newFeed;
            }
        }.execute();
    }

    /**
     * 查看某人的所有分享
     */

    public List<FeedModel> getUserFeed(final UserModel user, final long lastId, final int length) {
        return new BizTemplate<List<FeedModel>>("getUserFeed") {

            @Override
            protected void checkParams() throws BizException {
                if (user == null || user.getId() <= 0) {
                    throw new BizException(ERROR_USERID_IS_EMPTY);
                }
            }

            @Override
            protected List<FeedModel> process() throws Exception {
                List<FeedModel> result = feedDao.getUserFeed(user.getId(), lastId, length);
                if (result == null || result.size() == 0) {
                    return Collections.emptyList();
                }
                Set<Long> refFeedIds = Sets.newHashSetWithExpectedSize(result.size());
                for (FeedModel feed : result) {
                    if (feed != null && feed.getRefFeed() != null && feed.getRefFeed().getId() > 0) {
                        refFeedIds.add(feed.getRefFeed().getId());
                    }
                }
                Map<Long, FeedModel> refFeedMap = feedDao.getFeedById(refFeedIds);
                if (refFeedMap == null) {
                    refFeedMap = Collections.emptyMap();
                }
                for (FeedModel feed : result) {
                    if (feed != null && feed.getRefFeed() != null && feed.getRefFeed().getId() > 0) {
                        feed.setRefFeed(refFeedMap.get(feed.getRefFeed().getId()));
                    } else {
                        feed.setRefFeed(null);
                    }
                }
                return result;
            }
        }.execute();
    }

    /**
     * 查看某人的timeline
     */
    public List<FeedModel> getUserTimeline(final UserModel user, final long lastId, final int length) {
        return new BizTemplate<List<FeedModel>>("getUserFeed") {

            @Override
            protected void checkParams() throws BizException {
                if (user == null || user.getId() <= 0) {
                    throw new BizException(ERROR_USERID_IS_EMPTY);
                }
            }

            @Override
            protected List<FeedModel> process() throws Exception {
                List<TimelineModel> timelines = timelineDao.getFeedTimeline(user.getId(), lastId, length);
                if (timelines == null || timelines.size() == 0) {
                    return Collections.emptyList();
                }
                Set<Long> feedIds = Sets.newHashSetWithExpectedSize(timelines.size());
                Iterator<TimelineModel> iter = timelines.iterator();
                while (iter.hasNext()) {
                    TimelineModel timeline = iter.next();
                    if (timeline == null || timeline.getFeed() <= 0) {
                        iter.remove();
                        continue;
                    }
                    feedIds.add(timeline.getFeed());
                    if (timeline.getRefFeed() > 0) {
                        feedIds.add(timeline.getRefFeed());
                    }
                }
                Map<Long, FeedModel> feedMap = feedDao.getFeedById(feedIds);
                if (feedMap == null) {
                    feedMap = Collections.emptyMap();
                }
                List<FeedModel> result = Lists.newArrayListWithCapacity(timelines.size());
                for (TimelineModel timeline : timelines) {
                    FeedModel feed = feedMap.get(timeline.getFeed());
                    if (feed == null) {
                        continue;
                    }
                    feed.setId(timeline.getId());
                    feed.setRefFeed(feedMap.get(timeline.getRefFeed()));
                    result.add(feed);
                }
                return result;
            }
        }.execute();
    }


}
