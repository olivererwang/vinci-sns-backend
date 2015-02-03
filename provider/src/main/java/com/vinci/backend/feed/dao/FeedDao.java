package com.vinci.backend.feed.dao;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vinci.backend.feed.model.FeedModel;
import com.vinci.common.base.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.vinci.backend.Constants.*;

/**
 * 原始发的feed内容的表
 * Created by tim@vinci on 15-2-2.
 */
@Repository
public class FeedDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 插入一个feed
     */
    public FeedModel insertFeed(final FeedModel feedModel) {
        if (feedModel == null) {
            throw new BizException(ERROR_FEED_NULL);
        }
        if (StringUtils.isEmpty(feedModel.getContent()) || StringUtils.isEmpty(feedModel.getFeedType())) {
            throw new BizException(ERROR_FEED_CONTENT_NULL);
        }
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            int rowCount = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement statement = con.prepareStatement("INSERT INTO " + FEED_DATABASE_NAME
                                    + ".origin_feed (userid,feed_type,content,ref_feed_id) VALUES (?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS);
                    statement.setLong(1, feedModel.getUserid());
                    statement.setString(2, feedModel.getFeedType());
                    statement.setString(3, feedModel.getContent());
                    if (feedModel.getRefFeed() == null) {
                        statement.setLong(4, 0);
                    } else {
                        statement.setLong(4, feedModel.getRefFeed().getId());
                    }
                    return statement;
                }
            }, keyHolder);
            if (rowCount != 1 || keyHolder.getKey() == null || keyHolder.getKey().longValue() <= 0L) {
                throw new BizException(ERROR_UNKNOWN_ERROR);
            }
            feedModel.setId(keyHolder.getKey().longValue());
            return feedModel;
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }
    }

    /**
     * 获取一个人发的feed
     */
    public List<FeedModel> getUserFeed(final long userId, final long lastId, final int length) {
        if (userId < 0) {
            throw new BizException(ERROR_ATTENTION_USERID_IS_NEGATIVE);
        }
        try {

            StringBuilder sql = new StringBuilder("select id,feed_type,content,ref_feed_id,create_date " +
                    "from ").append(FEED_DATABASE_NAME).append(".origin_feed force index (`idx_user`) where " +
                    "userid=?");
            if (lastId > 0) {
                sql.append(" and id<").append(lastId);
            }
            sql.append(" order by id desc");
            if (length > 0) {
                sql.append(" limit ").append(length);
            }
            return jdbcTemplate.query(sql.toString(), new ResultSetExtractor<List<FeedModel>>() {
                @Override
                public List<FeedModel> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    List<FeedModel> result = Lists.newArrayListWithCapacity(length);
                    while (rs.next()) {
                        FeedModel feed = new FeedModel();
                        feed.setId(rs.getLong("id"));
                        feed.setUserid(userId);
                        feed.setContent(rs.getString("content"));
                        feed.setFeedType(rs.getString("feed_type"));
                        feed.setCreateDate(rs.getDate("create_date"));
                        long refFeedId = rs.getLong("ref_feed_id");
                        if (refFeedId > 0) {
                            feed.setRefFeed(new FeedModel());
                            feed.getRefFeed().setId(refFeedId);
                        }
                        result.add(feed);
                    }
                    return result;
                }
            }, userId);
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }
    }

    /**
     * 通过feed id获取feed内容
     */
    public Map<Long, FeedModel> getFeedById(final Set<Long> feedIds) {
        if (feedIds == null || feedIds.size() == 0) {
            return Collections.emptyMap();
        }
        try {

            StringBuilder sql = new StringBuilder("select id,userid,feed_type,content,ref_feed_id,create_date " +
                    "from ").append(FEED_DATABASE_NAME).append(".origin_feed where id in (")
                    .append(Joiner.on(',').skipNulls().join(feedIds)).append(")");
            return jdbcTemplate.query(sql.toString(), new ResultSetExtractor< Map<Long,FeedModel>>() {
                @Override
                public  Map<Long,FeedModel> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Map<Long,FeedModel> result = Maps.newHashMapWithExpectedSize(feedIds.size());
                    while (rs.next()) {
                        FeedModel feed = new FeedModel();
                        feed.setId(rs.getLong("id"));
                        feed.setUserid(rs.getLong("userid"));
                        feed.setContent(rs.getString("content"));
                        feed.setFeedType(rs.getString("feed_type"));
                        feed.setCreateDate(rs.getDate("create_date"));
                        long refFeedId = rs.getLong("ref_feed_id");
                        if (refFeedId > 0) {
                            feed.setRefFeed(new FeedModel(refFeedId));
                        }
                        result.put(feed.getId(),feed);
                    }
                    return result;
                }
            });
        } catch (DataAccessException e) {
            throw new BizException(e, ERROR_DATABASE_FAILED);
        }
    }
}
