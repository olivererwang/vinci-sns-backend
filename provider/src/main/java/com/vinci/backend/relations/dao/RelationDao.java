package com.vinci.backend.relations.dao;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.vinci.backend.relations.model.Attention;
import com.vinci.common.base.exception.BizException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.vinci.backend.Constants.*;

/**
 * Created by tim@vinci on 15-1-30.
 */
@Repository
public class RelationDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public void createRelation(final long sourceId, final List<Long> dstIds) {
        if (sourceId < 0) {
            throw new BizException(RELATION_ERROR_USERID_IS_NEGATIVE);
        }
        if (dstIds == null || dstIds.size() == 0) {
            throw new BizException(RELATION_ERROR_FOLLOWER_ID_IS_NEGATIVE);
        }
        String sql = "insert into " + RELATION_DATABASE_NAME + ".relation (source_user,dst_user) values ";
        List<String> insertValues = Lists.newArrayListWithCapacity(dstIds.size());
        for (long id : dstIds) {
            if (id <= 0) {
                throw new BizException(RELATION_ERROR_FOLLOWER_ID_IS_NEGATIVE);
            }
            insertValues.add("(" + sourceId + "," + id + ")");
        }
        try {
            jdbcTemplate.update(sql + Joiner.on(',').join(insertValues));
        } catch (DuplicateKeyException e) {
            throw new BizException(e, RELATION_ERROR_HAS_ATTENTION);
        } catch (DataAccessException e) {
            throw new BizException(e, RELATION_ERROR_DATABASE_FAILED);
        }
    }

    public int getAttentionCount(final long sourceId) {
        if (sourceId <= 0) {
            throw new BizException(RELATION_ERROR_USERID_IS_NEGATIVE);
        }
        try {
            return jdbcTemplate.queryForObject("SELECT count(*) FROM " + RELATION_DATABASE_NAME + ".relation WHERE source_user=?"
                    , Integer.class, sourceId);
        } catch (DataAccessException e) {
            throw new BizException(e, RELATION_ERROR_DATABASE_FAILED);
        }
    }

    public int getFollowerCount(final long dstId) {
        if (dstId <= 0) {
            throw new BizException(RELATION_ERROR_USERID_IS_NEGATIVE);
        }
        try {
            return jdbcTemplate.queryForObject("SELECT count(*) FROM " + RELATION_DATABASE_NAME + ".relation WHERE dst_user=?"
                    , Integer.class, dstId);
        } catch (DataAccessException e) {
            throw new BizException(e, RELATION_ERROR_DATABASE_FAILED);
        }
    }

    /**
     * 获取关注关系
     *
     * @param isGetAttentions true：获取关注的人 false：获取粉丝
     */
    public List<Attention> getAttentions(final long userId, final long lastId, final int length, final boolean isGetAttentions) {
        if (userId < 0) {
            throw new BizException(RELATION_ERROR_USERID_IS_NEGATIVE);
        }
        try {

            StringBuilder sql = new StringBuilder("select id,").append(isGetAttentions ? "dst_user" : "source_user")
                    .append(" as userid from ").append(RELATION_DATABASE_NAME).append(".relation")
                    .append(" use index (`").append(isGetAttentions?"idx_source_user":"idx_dst_user").append("`)")
                    .append(" where ").append(isGetAttentions ? "source_user" : "dst_user").append("=?");
            if (lastId > 0) {
                sql.append(" and id<").append(lastId);
            }
            sql.append(" order by id desc");
            if (length > 0) {
                sql.append(" limit ").append(length);
            }
            return jdbcTemplate.query(sql.toString(), new ResultSetExtractor<List<Attention>>() {
                @Override
                public List<Attention> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    List<Attention> result = Lists.newArrayListWithCapacity(length);
                    while (rs.next()) {
                        Attention attention = new Attention();
                        attention.setAttention(isGetAttentions);
                        attention.setSourceUserId(userId);
                        attention.setId(rs.getLong("id"));
                        attention.setDstUserId(rs.getLong("dst_user"));
                        attention.setCreateDate(rs.getDate("create_date"));
                        result.add(attention);
                    }
                    return result;
                }
            }, userId);
        } catch (DataAccessException e) {
            throw new BizException(e, RELATION_ERROR_DATABASE_FAILED);
        }
    }

    /**
     * 判断 source 是否关注dst
     */
    public boolean isAttention(long sourceId, long dstId) {
        if (sourceId <= 0 || dstId <= 0) {
            throw new BizException(RELATION_ERROR_USERID_IS_NEGATIVE);
        }
        try {

            return jdbcTemplate.query("SELECT id FROM " + RELATION_DATABASE_NAME + ".relation WHERE source_user=? AND dst_user=?"
                    , new ResultSetExtractor<Boolean>() {
                        @Override
                        public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
                            return rs.next();
                        }
                    }, sourceId, dstId);
        } catch (DataAccessException e) {
            throw new BizException(e, RELATION_ERROR_DATABASE_FAILED);
        }
    }
}
