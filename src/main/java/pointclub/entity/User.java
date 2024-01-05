package pointclub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import pointclub.entity.chat.ChatRoom;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int serverId;
    private String name;
    private String token;

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    @ToString.Exclude
    private Set<ChatRoom> charRooms = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(serverId, user.serverId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
