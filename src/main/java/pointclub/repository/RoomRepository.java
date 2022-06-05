package pointclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pointclub.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}