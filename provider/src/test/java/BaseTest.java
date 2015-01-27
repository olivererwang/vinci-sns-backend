import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by tim@vinci on 15-1-27.
 */
public class BaseTest {
    protected static ClassPathXmlApplicationContext applicationContext;

    @BeforeClass
    public static void beforeClass() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void executeSql(DataSource ds, String sql) {
        Connection c = null;
        Statement st = null;
        try {
            c = ds.getConnection();
            st = c.createStatement();
            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (c != null && !c.isClosed())
                    c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (st != null && !st.isClosed())
                    st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        try {
            applicationContext.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() throws Exception {
    }
}