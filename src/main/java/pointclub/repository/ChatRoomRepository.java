package pointclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pointclub.entity.chat.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
}