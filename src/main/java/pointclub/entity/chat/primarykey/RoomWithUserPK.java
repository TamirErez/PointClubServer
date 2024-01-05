package pointclub.entity.chat.primarykey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomWithUserPK implements Serializable {
    private int room;
    private int user;
}
