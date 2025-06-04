import com.server.ServerApplication;
import com.server.ai.AiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class Agriculture {

    @Autowired
    private AiService aiService;

    @Test
    public void testAIService() {
        aiService.chatStream("Tell me a joke").toStream().forEach(System.out::print);
    }
}
