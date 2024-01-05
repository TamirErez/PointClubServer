package pointclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pointclub.entity.chat.ChatRoomWithUser;

public interface UsersToChatRoomsRepository extends JpaRepository<ChatRoomWithUser, Integer> {
}
