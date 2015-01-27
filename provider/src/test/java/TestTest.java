import com.vinci.V1;
import org.junit.Test;

/**
 * Created by tim@vinci on 15-1-27.
 */
public class TestTest extends BaseTest{

    @Test
    public void test1() {
        V1 test = applicationContext.getBean(V1.class);
        test.test();
    }
}
