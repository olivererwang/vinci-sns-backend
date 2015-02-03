import com.vinci.backend.domain.user.dao.DeviceDao;
import com.vinci.backend.domain.user.model.DeviceInfo;
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
        info.setMacAddr("cccccc");

        deviceDao.insert(info);

        System.out.println("--------"+deviceDao.getDeviceInfo("edddd","cccccc"));
    }

    @Test
    public void test1() {

        System.out.println(deviceDao.getDeviceInfoById(6));
    }

}
