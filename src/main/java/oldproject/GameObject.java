package oldproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GameObject {
    List<User> users = new ArrayList<>();
    List<String> chat = new ArrayList<>();
    public String name;

    public String addUser(User newUser) {
        if (!users.contains(newUser)) {
            users.add(newUser);
        }
        return "welcome";
    }

    public void removeUser(Integer id) {
        User toRemove = null;
        for (User u : users)
            if (u.id == id)
                toRemove = u;
        users.remove(toRemove);
    }

    //message is of type 'gamemessage: <username>: <msg>'
    public void sendMessage(String msg, User sender) throws IOException {
        msg = msg.split(":")[1];
        String newMsg = sender.name + ":" + msg;
        chat.add(newMsg);
        msg = "gamemessage:" + newMsg;
        for (User u : users) {
            System.out.println(u.name);
            if (u != null && u.name != null && u.out != null && u != sender) {
                System.out.println("sending: " + msg);
                u.out.writeUTF(msg);
                u.out.flush();
            }
        }
    }

    //message is of type chat:<msg>
    public void sendChat(User user) {
        Thread temp = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (String s : chat) {
                    try {
                        user.out.writeUTF("chat:" + s);
                        user.out.flush();
                    } catch (Exception e) {
                        System.out.println("Failed to send chat");
                    }
                }
            }
        });
        temp.start();
    }

    public abstract String main(String input);

    //first sen gamephoto declaring the photo size, than send byte array
    public void sendPhoto(byte[] imgBytes, User sender) throws IOException {
        for (User u : users) {
            System.out.println(u.name);
            if (u.name != null && u.out != null && u != sender) {
                System.out.println("sending: " + "gamephoto:" + imgBytes.length);
                u.out.writeUTF("gamephoto:" + imgBytes.length);
                u.out.flush();
                u.out.write(imgBytes, 0, imgBytes.length);
                u.out.flush();
            }
        }
    }
}

