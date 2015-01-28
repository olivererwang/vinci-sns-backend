import com.vinci.V1;
import com.vinci.backend.user.dao.DeviceDao;
import org.junit.Test;

/**
 * Created by tim@vinci on 15-1-27.
 */
public class UserDaoTest extends BaseTest{

    @Test
    public void test1() {
        DeviceDao dao  = applicationContext.getBean(DeviceDao.class);
        System.out.println(dao.getDeviceInfoById(1));
    }
}
