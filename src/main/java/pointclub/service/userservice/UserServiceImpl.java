package pointclub.service.userservice;

import org.springframework.stereotype.Service;
import pointclub.entity.Room;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public Room getRoomOfUser(int userId) {
        return new Room();
    }
}
