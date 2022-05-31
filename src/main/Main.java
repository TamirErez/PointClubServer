package main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.zip.DeflaterOutputStream;

public class Main {
    public static final int PORT = 31415;
    public static final int version = 12;
    public static final String PATH = "D:\\app\\";

    // connect game id to boom object to find specific game using the id
    private static Dictionary<Integer, Boom> boomDic;

    // connect game type to id to find available games
    private static Dictionary<String, Integer> gamesCounter;

    // save all games to keep track of which users are in which games
    private static List<GameObject> allGames;

    public static void main(String[] args) {
        boomDic = new Hashtable<>();
        gamesCounter = new Hashtable<>();
        gamesCounter.put("Boom", 0);
        allGames = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        try {
            // Create server socket
            ServerSocket srvSock = new ServerSocket(PORT);

            Integer clientNumber = -1;
            // Start client loop
            while (true) {
                // wait for new client
                System.out.println("server is waiting");
                Socket clientSocket = srvSock.accept();
                clientNumber++;
                final int currentClient = clientNumber;

                // Create new Thread for a new client
                Thread newClient = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        DataOutputStream out = null;
                        DataInputStream in = null;
                        try {

                            // in & out channels for client
                            out = new DataOutputStream(clientSocket.getOutputStream());
                            in = new DataInputStream(clientSocket.getInputStream());
                        } catch (IOException e) {
                            System.out.println("Couldn't get I/O stream");
                        }

                        // find the user id
                        int userId;
                        try {
                            userId = Integer.parseInt(Thread.currentThread().getName());
                        } catch (Exception e) {
                            userId = currentClient;
                        }

                        User thisUser = new User();
                        thisUser.id = userId;
                        thisUser.name = "Anon";
                        thisUser.out = out;

                        while (true) {
                            try {
                                // get input
                                String input = "plskillme";
                                if (in != null) {
                                    input = in.readUTF().toLowerCase().trim();
                                }
                                LocalDateTime now = LocalDateTime.now();
                                System.out.println(dtf.format(now) + ">Server got: " + input);
                                String output = "testcomms";

                                if (input.equals("plskillme"))
                                    output = null;

                                    // let user enter a game
                                else if (input.startsWith("entergame")) {
                                    // input is of type 'entergame:<room name>'
                                    input = input.substring("entergame:".length());
                                    output = handleJoinGame(thisUser, input, output);
                                }

                                // leave game
                                else if (input.equals("leavegame")) {
                                    // try to remove user from all games
                                    removeUser();
                                    output = "leavegame:true";
                                }

                                // create game
                                else if (input.startsWith("creategame")) {
                                    // input is of type 'creategame:<game name>,<game arguments>...'
                                    input = input.substring("creategame:".length());
                                    output = handleCreateGame(out, thisUser, input);

                                    // user asks for some kind of information
                                } else if (input.startsWith("request")) {
                                    input = input.substring("request:".length());
                                    if (input.equals("update")) {
                                        handleUpdate(clientSocket, out);
                                    } else if (input.equals("roominfo")) {
                                        handleRoomNames(out);
                                    }

                                    // send message
                                    // expects gamemessage:<room name>,<message>
                                } else if (input.startsWith("gamemessage")) {
                                    input = input.substring("gamemessage:".length());
                                    handleGameMessage(input, thisUser);

                                    //send photo
                                    //expects gamephoto:<room name>,<photo size>
                                } else if (input.startsWith("gamephoto")) {
                                    input = input.substring("gamephoto:".length());
                                    handleGamePhoto(input, thisUser, in);

                                    // change user name
                                } else if (input.startsWith("name")) {
                                    input = input.substring("name:".length());
                                    thisUser.name = input;
                                    output = "name:true";

                                    // check version
                                } else if (input.startsWith("version")) {
                                    input = input.substring("version:".length());
                                    int userVersion = Integer.parseInt(input);
                                    if (userVersion < version)
                                        output = "version:false";
                                    else
                                        output = "version:true";

                                }

                                if (output == null) {
                                    out.close();
                                    in.close();
                                    clientSocket.close();
                                    return;
                                }
                                System.out.println("sending: " + output);
                                out.writeUTF(output);
                                out.flush();
                            } catch (EOFException e) {

                            } catch (IOException e) {
                                System.out.println("Coudln't accept new client :( #foreveralone");
                                e.printStackTrace();
                                removeUser();
                                if (clientSocket.isConnected())
                                    try {
                                        clientSocket.close();
                                        return;
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                            } catch (Exception e) {
                                removeUser();
                                e.printStackTrace();
                                if (clientSocket.isConnected())
                                    try {
                                        clientSocket.close();
                                        return;
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                            }

                        }
                    }

                    private void removeUser() {
                        int id = Integer.parseInt(Thread.currentThread().getName());
                        for (GameObject go : allGames)
                            go.removeUser(id);
                    }

                });

                newClient.setName(clientNumber.toString());
                newClient.start();
            }
        } catch (Exception e) {
            System.out.println("Couldn't create server socket");
        }

    }


    // sends for each room room:<name>,<user count>
    private static void handleRoomNames(DataOutputStream out) throws IOException {
        for (GameObject go : allGames) {
            System.out.println("sending: room info");
            out.writeUTF("room:" + go.name + "," + go.users.size());
            out.flush();
        }
    }

    private static String handleJoinGame(User thisUser, String input, String output) {
        Object gameInstance = getGame(input);

        // no game found
        if (gameInstance instanceof String)
            output = "entergame:" + (String) gameInstance;

            // game found
        else if (gameInstance instanceof GameObject) {
            // create new user
            /*
             * User newUser = new User(); newUser.id = userId; newUser.isAlive = true;
             * newUser.out = out; newUser.name = "Anon";
             */

            // add the user if possible
            GameObject gi = ((GameObject) gameInstance);
            output = gi.addUser(thisUser);
            if (output.equals("welcome")) {
                gi.sendChat(thisUser);
            }
            output = "entergame:" + gi.name;
        }
        return output;
    }

    private static String handleCreateGame(OutputStream out, User thisUser, String input) {
        String output;
        String[] split = input.split(",");
        if (split[0].equals("boom")) {
            Boom newBoom = new Boom(Integer.parseInt(split[1]));

            // get gameid
            int gameId = allGames.size();
            gameId++;

            newBoom.name = gameId + "";

            // update dics
            gamesCounter.put(gameId + "", gameId);
            boomDic.put(gameId, newBoom);
            allGames.add(newBoom);

            /*
             * User thisUser = new User(); thisUser.isAlive = true;
             *
             * thisUser.id = userId; thisUser.name = "Anon";
             *
             * thisUser.out = out;
             */

            newBoom.addUser(thisUser);
            output = "creategame:" + newBoom.name;
        } else
            output = "creategame:false";
        return output;
    }

    //gamemessage:<room name>,<msg>
    private static void handleGameMessage(String input, User user) throws IOException {
        // TODO: make message game specific
        String[] split = input.split(",");
        String name = split[0];
        String message = split[1];
        GameObject game = (GameObject) getGame(name);
        game.sendMessage(message, user);
    }

    private static void handleGamePhoto(String input, User thisUser, DataInputStream in) throws IOException {
        String[] split = input.split(",");
        String name = split[0];
        int size = Integer.parseInt(split[1]);
        GameObject game = (GameObject) getGame(name);
        byte[] imgBytes = new byte[size];

        /*int current, bytesRead;
        bytesRead = in.read(imgBytes, 0, imgBytes.length);
        current = bytesRead;
*/

        /*do {
            bytesRead =
                    in.read(imgBytes, current, (imgBytes.length - current));
            if (bytesRead >= 0) current += bytesRead;
            if (bytesRead > 0)
                System.out.println("read " + bytesRead + " and total " + current);

        } while (bytesRead > -1 && current < size);
*/
        int read = 0, totalGot = 0;
        while (read > -1 && totalGot < size) {
            read = in.read(imgBytes, totalGot, size - totalGot);
            if (read > -1)
                totalGot += read;
        }
        System.out.println("Expected " + size + " bytes, and got " + totalGot + " bytes");

        game.sendPhoto(imgBytes, thisUser);
    }

    private static void handleUpdate(Socket clientSocket, DataOutputStream out) throws IOException {
        String path = PATH + "app-debug.apk";
        File myFile = new File(path);
        FileInputStream fis = new FileInputStream(myFile);
        /*
         * byte[] mybytearray = new byte[(int) myFile.length()];
         *
         * BufferedInputStream bis = new BufferedInputStream(fis); bis.read(mybytearray,
         * 0, mybytearray.length);
         */
        //OutputStream os = clientSocket.getOutputStream();
        byte[] mybytearray = compressByteArray(fis);
        out.writeUTF("update:" + (int) myFile.length());
        out.flush();
        System.out.println("Sending apk(" + mybytearray.length + " bytes)");

        out.write(mybytearray, 0, mybytearray.length);
        out.flush();
        System.out.println("Done.");
        fis.close();
        // bis.close();
    }

    private static Object getGame(String roomName) {
        Integer id = gamesCounter.get(roomName);
        if (id == null)
            return id;
        return boomDic.get(id);
        /*
         * switch (gameType) { case "boom": try { return
         * boomDic.elements().nextElement(); } catch (Exception e) { return "No " +
         * gameType + " with that id"; } } return null;
         */
    }

    public static byte[] compressByteArray(FileInputStream fis) throws IOException {
        Path path = Paths.get(PATH, "zipped_" + version);
        // Path path = Paths.get(PATH, "app-debug.apk");
        if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
            DeflaterOutputStream dos = new DeflaterOutputStream(new FileOutputStream(path.toString()));
            int ch;
            while ((ch = fis.read()) != -1) {
                dos.write((byte) ch);
            }
            dos.close();
            fis.close();
        }

        File myFile = new File(path.toString());
        byte[] mybytearray = new byte[(int) myFile.length()];
        fis = new FileInputStream(myFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        bis.read(mybytearray, 0, mybytearray.length);
        bis.close();
        return mybytearray;
    }

    /*
     * public static byte[] compressByteArray(byte[] bytes){
     *
     * ByteArrayOutputStream baos = null; Deflater dfl = new Deflater();
     * dfl.setLevel(Deflater.BEST_COMPRESSION); dfl.setInput(bytes); dfl.finish();
     * baos = new ByteArrayOutputStream(); byte[] tmp = new byte[4*1024]; try{
     * while(!dfl.finished()){ int size = dfl.deflate(tmp); baos.write(tmp, 0,
     * size); } } catch (Exception ex){
     *
     * } finally { try{ if(baos != null) baos.close(); } catch(Exception ex){} }
     *
     * return baos.toByteArray(); }
     */

}
