package pointclub.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "messages")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int serverId;
    private String content;
    @OneToOne
    @JoinColumn(name = "sender", referencedColumnName = "id")
    private User sender;

    @OneToOne
    @JoinColumn(name = "room", referencedColumnName = "id")
    private Room room;
}
