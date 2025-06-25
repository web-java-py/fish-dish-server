import com.server.ServerApplication;
import com.server.ai.AiService;
import com.server.fisco.FiscoBcos;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Agriculture {

    @Autowired
    private AiService aiService;

    @Autowired
    private FiscoBcos fiscoBcos;

    @Test
    public void testAIService() {
        aiService.chatStream("Tell me a joke").toStream().forEach(System.out::print);
    }
}
