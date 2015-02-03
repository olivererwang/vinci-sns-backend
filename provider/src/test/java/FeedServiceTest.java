import com.google.common.collect.Lists;
import com.vinci.backend.feed.model.FeedModel;
import com.vinci.backend.feed.service.FeedService;
import com.vinci.backend.relations.service.RelationService;
import com.vinci.backend.user.model.UserModel;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class FeedServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Resource
    private FeedService feedService;
    @Resource
    private RelationService relationService;
    @Resource
    private JdbcTemplate jdbcTemplate;


    @Before
    @Transactional
    public void setUp() {
        UserModel user = new UserModel();
        user.setId(1234L);
        user.setNickName("abc");
        UserModel dstUser = new UserModel();
        dstUser.setId(1L);
        relationService.createAttention(user, Lists.newArrayList(dstUser));
        relationService.createAttention(dstUser, Lists.newArrayList(user));
    }

    @Test
    public void testInsertFeed() {
        UserModel sourceUser = new UserModel(1234L, "abc");
        FeedModel feed = new FeedModel();
        feed.setFeedType("music");
        feed.setContent("abcdeffff");
        feed = feedService.insertFeed(sourceUser, feed);
        System.out.println("--------" + feed);

        System.out.println("---------" + feedService.getUserFeed(sourceUser, 0, 0));
        System.out.println("---------" + feedService.getUserTimeline(new UserModel(1L, "ttt"), 0, 0));
    }
}
