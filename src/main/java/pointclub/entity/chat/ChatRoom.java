package pointclub.entity.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import pointclub.entity.Room;
import pointclub.entity.User;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ChatRoom extends Room {
    @ManyToMany
    @JoinTable(name = "users_chat_rooms",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    @ToString.Exclude
    private Set<User> users = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "room")
    @JsonIgnore
    @ToString.Exclude
    private Set<Message> messages = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return Objects.equals(serverId, chatRoom.serverId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
