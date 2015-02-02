package com.vinci.backend.feed.dao;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.vinci.backend.feed.model.FeedModel;
import com.vinci.backend.feed.model.TimelineModel;
import com.vinci.common.base.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.vinci.backend.Constants.*;
/**
 * 每个人的timeline表的dao
 * Created by tim@vinci on 15-2-2.
 */
@Repository
public class TimelineDao {

    private static final Logger logger = LoggerFactory.getLogger(TimelineDao.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 插入一个feed
     */
    public void insertFeed(final FeedModel feedModel , final List<Long> users) {
        if (feedModel == null) {
            throw new BizException(FEED_ERROR_FEED_NULL);
        }
        if (StringUtils.isEmpty(feedModel.getContent()) || StringUtils.isEmpty(feedModel.getFeedType())) {
            throw new BizException(FEED_ERROR_FEED_CONTENT_NULL);
        }
        if (users == null || users.size() == 0) {
            logger.warn("要插入一个users列表为空的feed");
            return;
        }
        try {
            StringBuilder sql = new StringBuilder("insert into ").append(FEED_DATABASE_NAME).append(".feed_timeline  (userid,feed_id,ref_feed_id) values ");
            List<String> params = Lists.newArrayListWithCapacity(users.size());
            for (Long user : users) {
                if (user != null) {
                    params.add("("+feedModel.getUserid()+","+feedModel.getId()+","+(feedModel.getRefFeed()==null?"0":feedModel.getRefFeed().getId())+")");
                }
            }
            sql.append(Joiner.on(',').skipNulls().join(params));
            jdbcTemplate.update(sql.toString());
        } catch (DataAccessException e) {
            throw new BizException(e, FEED_ERROR_DATABASE_ERROR);
        }
    }

    /**
     * 获取一个人发的feed
     */
    public List<TimelineModel> getFeedTimeline(final long userId, final long lastId, final int length) {
        if (userId < 0) {
            throw new BizException(FEED_ERROR_USERID_IS_NEGATIVE);
        }
        try {

            StringBuilder sql = new StringBuilder("select id,feed_id,ref_feed_id,create_date " +
                    "from ").append(FEED_DATABASE_NAME).append(".feed_timeline force index (`idx_user`) where " +
                    "userid=?");
            if (lastId > 0) {
                sql.append(" and id<").append(lastId);
            }
            sql.append(" order by id desc");
            if (length > 0) {
                sql.append(" limit ").append(length);
            }
            return jdbcTemplate.query(sql.toString(), new ResultSetExtractor<List<TimelineModel>>() {
                @Override
                public List<TimelineModel> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    List<TimelineModel> result = Lists.newArrayListWithCapacity(length);
                    while (rs.next()) {
                        TimelineModel model = new TimelineModel();
                        model.setId(rs.getLong("id"));
                        model.setUserid(userId);
                        model.setCreateDate(rs.getDate("create_date"));
                        model.setFeed(new FeedModel(rs.getLong("feed_id")));
                        long refFeedId = rs.getLong("ref_feed_id");
                        if (refFeedId > 0) {
                            model.setRefFeed(new FeedModel(refFeedId));
                        }
                        result.add(model);
                    }
                    return result;
                }
            }, userId);
        } catch (DataAccessException e) {
            throw new BizException(e, FEED_ERROR_DATABASE_ERROR);
        }
    }
}
