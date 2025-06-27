import com.server.ServerApplication;
import com.server.ai.AiService;
import com.server.fisco.FiscoBcos;
import org.fisco.bcos.sdk.client.Client;
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

    @Autowired
    private Client client;

    @Test
    public void testAIService() {
        aiService.chatStream("Tell me a joke").toStream().forEach(System.out::print);
    }

    @Test
    public void testFiscoBcosService() {
//        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
//        HelloWorld helloWorld = null;
//        try {
//            helloWorld = HelloWorld.deploy(client, cryptoKeyPair);
//            // 调用HelloWorld合约的set接口
//            TransactionReceipt receipt = helloWorld.set("Hello, fisco");
//            System.out.println(receipt.getContractAddress());
//            // 调用HelloWorld合约的get接口
//            String getValue = helloWorld.get();
//            System.out.println(getValue);
//        } catch (ContractException e) {
//            e.printStackTrace();
//        }
    }
}
