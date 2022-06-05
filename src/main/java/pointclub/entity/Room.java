package pointclub.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "rooms", schema = "pointclub")
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
}
