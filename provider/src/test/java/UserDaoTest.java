import com.vinci.backend.user.dao.DeviceDao;
import com.vinci.backend.user.dao.UserDao;
import org.junit.Before;

/**
 * Created by tim@vinci on 15-1-30.
 */
public class UserDaoTest  extends BaseTest {
    private UserDao userDao;


    @Before
    public void setUp() {
        userDao= applicationContext.getBean(UserDao.class);
    }
}
