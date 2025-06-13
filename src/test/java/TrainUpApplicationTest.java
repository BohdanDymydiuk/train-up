import com.example.trainup.TrainUpApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TrainUpApplication.class)
@ActiveProfiles("test")
public class TrainUpApplicationTest {
    @Test
    void contextLoads() {
    }
}
