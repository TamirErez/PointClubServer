package pointclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("pointclub.entity")
@EnableJpaRepositories("pointclub.repository")
public class PointClubServerRunner {

    public static void main(String[] args) {
        SpringApplication.run(PointClubServerRunner.class, args);
    }
}
