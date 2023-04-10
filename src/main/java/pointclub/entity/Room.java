package pointclub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int serverId;
    private String name;

    @ManyToMany
    @JoinTable(name = "users_rooms",
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
        Room room = (Room) o;
        return Objects.equals(serverId, room.serverId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
