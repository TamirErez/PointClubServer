package pointclub;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.FileInputStream;

@SpringBootApplication
@EntityScan("pointclub.entity")
@EnableJpaRepositories("pointclub.repository")
public class PointClubServerRunner {

    public static void main(String[] args) {
        SpringApplication.run(PointClubServerRunner.class, args);
        initFirebase();
    }

    @SneakyThrows
    private static void initFirebase() {
        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/firebaseCredentials.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }
}
