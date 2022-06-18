package pointclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pointclub.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
