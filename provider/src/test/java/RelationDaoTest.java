import com.google.common.collect.Lists;
import com.vinci.backend.domain.relations.dao.RelationDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

/**
 * Created by tim@vinci on 15-1-30.
 */
public class RelationDaoTest extends BaseTest{

    private RelationDao dao;
    private DataSource dataSource;
    @Before
    public void setUp() {
        dao = applicationContext.getBean(RelationDao.class);
        dataSource = applicationContext.getBean(DataSource.class);
//        executeSql(dataSource,"delete from relations.relation");
    }

    @Test
    public void testInsert() {
        dao.createRelation(1234L, Lists.<Long>newArrayList(1L, 2L, 3L, 4L));
        dao.createRelation(12,Lists.<Long>newArrayList(1L));
        dao.createRelation(13,Lists.<Long>newArrayList(1L));
        dao.createRelation(14,Lists.<Long>newArrayList(1L));
        dao.createRelation(15,Lists.<Long>newArrayList(1L));
        dao.createRelation(16,Lists.<Long>newArrayList(1L));
        dao.createRelation(17,Lists.<Long>newArrayList(1L));
    }

    @Test
    public void testGetCount() {
        testInsert();
        Assert.assertEquals(4,dao.getAttentionCount(1234L));
        Assert.assertEquals(7,dao.getFollowerCount(1L));
    }

    @Test
    public void testGetAttention() {
//        testInsert();
        System.out.println(dao.getAttentions(1234L, 24, 1, true));
        System.out.println(dao.getAttentions(1L,28,3,false));
    }
}
