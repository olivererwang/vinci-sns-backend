import com.vinci.V1;
import com.vinci.backend.user.dao.DeviceDao;
import com.vinci.backend.user.model.DeviceInfo;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by tim@vinci on 15-1-27.
 */
public class DeviceDaoTest extends BaseTest{

    private DeviceDao deviceDao;


    @Before
    public void setUp() {
        deviceDao = applicationContext.getBean(DeviceDao.class);
    }

    @Test
    public void insert() {
        DeviceInfo info = new DeviceInfo();
        info.setImei("edddd");
        info.setUserId("yyyyy");
        info.setMacAddr("cccccc");

        info = deviceDao.insert(info);

        System.out.println("--------"+info);
    }

    @Test
    public void test1() {

        System.out.println(deviceDao.getDeviceInfoById(6));
    }


    @Test
    public void testGet() {
        System.out.println(deviceDao.getDeviceInfo("edddd","cccccc"));
    }
}
