package pointclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pointclub.entity.RoomWithUser;

public interface UsersToRoomsRepository extends JpaRepository<RoomWithUser, Integer> {
}
