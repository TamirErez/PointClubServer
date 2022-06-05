package pointclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pointclub.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
