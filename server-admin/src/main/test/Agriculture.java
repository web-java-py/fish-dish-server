import com.server.ServerApplication;
import com.server.ai.AIService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class Agriculture {

    @Autowired
    private AIService aiService;

    @Test
    public void testAIService() {
        System.out.println(aiService.chat("你好"));
    }
}
