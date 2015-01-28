import com.vinci.backend.user.dao.DeviceDao;
import com.vinci.common.base.i18n.I18NResource;
import com.vinci.common.base.i18n.MessageType;
import org.junit.Test;

import java.util.Locale;

/**
 * Created by tim@vinci on 15-1-27.
 */
public class I18NTest extends BaseTest{

    @Test
    public void test1() {
        I18NResource resource = applicationContext.getBean(I18NResource.class);
        String s = resource.getMessage(Locale.US, MessageType.errorcode,"1000001");
        System.out.println(s);
    }
}
