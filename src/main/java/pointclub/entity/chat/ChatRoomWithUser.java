package pointclub.entity.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pointclub.entity.chat.primarykey.RoomWithUserPK;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@IdClass(RoomWithUserPK.class)
@Table(name = "users_chat_rooms")
public class ChatRoomWithUser {
    @Id
    @Column(name = "room_id")
    private int room;
    @Id
    @Column(name = "user_id")
    private int user;
}
