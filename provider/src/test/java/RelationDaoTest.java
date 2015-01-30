import com.vinci.backend.relations.dao.RelationDao;
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
        executeSql(dataSource,"delete from relations.relation");
    }

    @Test
    public void testInsert() {
        dao.createRelation(1234L,1,2,3,4);
        dao.createRelation(12,1);
        dao.createRelation(13,1);
        dao.createRelation(14,1);
        dao.createRelation(15,1);
        dao.createRelation(16,1);
        dao.createRelation(17,1);
    }

    @Test
    public void testGetCount() {
        testInsert();
        Assert.assertEquals(4,dao.getAttentionCount(1234L));
        Assert.assertEquals(7,dao.getFollowerCount(1L));
    }

    @Test
    public void testGetAttention() {
        testInsert();
        System.out.println(dao.getAttentions(1234L, 0, 1, true));
        System.out.println(dao.getAttentions(1L,0,3,false));
    }
}
