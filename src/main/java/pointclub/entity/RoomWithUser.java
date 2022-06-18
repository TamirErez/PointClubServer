package pointclub.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pointclub.entity.primarykey.RoomWithUserPK;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@IdClass(RoomWithUserPK.class)
@Table(name = "users_rooms", schema = "pointclub")
public class RoomWithUser {
    @Id
    @Column(name = "room_id")
    private int room;
    @Id
    @Column(name = "user_id")
    private int user;
}
