package pointclub.service.userservice;

import pointclub.entity.Room;

public interface UserService {
    Room getRoomOfUser(int userId);
}
