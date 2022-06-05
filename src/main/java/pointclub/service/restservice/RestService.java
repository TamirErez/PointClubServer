package pointclub.service.restservice;

import java.net.http.HttpResponse;

public interface RestService {
    HttpResponse<String> postToRoom(String message, long id);
}
