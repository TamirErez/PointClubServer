package pointclub.entity.chat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pointclub.entity.User;
import pointclub.serializer.MessageSerializer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@JsonSerialize(using = MessageSerializer.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int serverId;

    //TODO: define custom length
    private String content;

    @ManyToOne
    @JoinColumn(name = "sender", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "room", referencedColumnName = "id")
    private ChatRoom chatRoom;

    private Date sendTime;
}
