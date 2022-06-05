package pointclub.service.restservice;

import java.net.http.HttpResponse;

public class RestServiceImpl implements RestService {
    private final String path;

    public RestServiceImpl(String path) {
        this.path = path;
    }

    @Override
    public HttpResponse<String> postToRoom(String message, long id) {
        System.out.println("Path is " + path);
        return null;
    }
}
