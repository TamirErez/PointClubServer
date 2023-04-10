package pointclub.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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

    private String content;

    @ManyToOne
    @JoinColumn(name = "sender", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "room", referencedColumnName = "id")
    private Room room;

    private Date sendTime;
}
