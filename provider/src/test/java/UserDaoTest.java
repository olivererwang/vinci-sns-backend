import com.vinci.backend.user.dao.DeviceDao;
import com.vinci.backend.user.dao.UserDao;
import com.vinci.backend.user.model.UserModel;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by tim@vinci on 15-1-30.
 */
public class UserDaoTest  extends BaseTest {
    private UserDao userDao;


    @Before
    public void setUp() {
        userDao= applicationContext.getBean(UserDao.class);
    }

    @Test
    public void testInsert() {
        UserModel userModel = new UserModel();
        userModel.setNickName("abcd");

        System.out.println(userDao.newUser(userModel));
    }

    @Test
    public void testGetUser() {
        System.err.println(userDao.getUser(1));
//        System.out.println(userDao.getUser("836f63e3-911c-43f4-a95b-fee9d82adf7"));
    }

    @Test
    public void testModify() {
        long userId = 1;
        UserModel userModel = userDao.getUser(1);
        userDao.changeUserDevice(userId,"cccc");
        testGetUser();
        userDao.modifyUserNickName(userId, "bbbb");
        testGetUser();
        userModel.getUserSettings().setHeadImgBaseUrl("aaaaaaa");
        userDao.modifyUserSettings(userId,userModel.getUserSettings(),userModel.getVersion());
        testGetUser();
    }
}
