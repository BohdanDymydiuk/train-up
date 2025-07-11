import com.cloudinary.Cloudinary;
import com.example.trainup.TrainUpApplication;
import com.example.trainup.repository.AthleteRepository;
import com.example.trainup.repository.GymOwnerRepository;
import com.example.trainup.repository.TrainerRepository;
import com.example.trainup.repository.UserCredentialsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TrainUpApplication.class)
@ActiveProfiles("test")
public class TrainUpApplicationTest {
    @MockBean
    private Cloudinary cloudinary;

    @MockBean
    private AthleteRepository athleteRepository;

    @MockBean
    private TrainerRepository trainerRepository;

    @MockBean
    private GymOwnerRepository gymOwnerRepository;

    @MockBean
    private UserCredentialsRepository userCredentialsRepository;

    @Test
    void contextLoads() {
    }
}
