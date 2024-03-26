package org.example;

import DB.DatabaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

public class MessageServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5001);
        out.println("Message Server started on port 5001");

        Map<String, String> pairs = new HashMap<>();
        Map<String , Socket> waitingClients = new HashMap<>();

        DatabaseConnection dbCon = new DatabaseConnection();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String usernameAndFriend = in.readLine();
            String[] s = usernameAndFriend.split(",");
            String username = s[0].trim();
            String friendUsername = s[1].trim();
            List<String> friends = dbCon.getFriends(username);

            boolean found = false;
            for (String friend : friends) {
                if (friend.equals(friendUsername)) {
                    found = true;
                    break;
                }
            }
            if(found) {

                pairs.put(username, friendUsername);

                Socket friendSocket = waitingClients.remove(friendUsername);
                if (friendSocket != null) {
                    out.println(username + " connected to " + friendUsername);
                    ClientHandler handler1 = new ClientHandler(clientSocket, friendSocket, username, friendUsername);
                    ClientHandler handler2 = new ClientHandler(friendSocket, clientSocket, friendUsername, username);
                    handler1.start();
                    handler2.start();
                } else {
                    out.println(friendUsername + " is not available for connection.");
                    out.println("wait for " + friendUsername + " to connect or type /end to exit.");
                    waitingClients.put(username, clientSocket);
                }
            }
            else {
                out.println("You are not friends with " + friendUsername);
            }

        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private Socket peerSocket;

    private String username;
    private String friendUsername;



    public ClientHandler(Socket clientSocket, Socket peerSocket, String username, String friendUsername) {
        this.clientSocket = clientSocket;
        this.peerSocket = peerSocket;
        this.username = username;
        this.friendUsername = friendUsername;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(peerSocket.getOutputStream(), true);

            while (true) {
                String message = in.readLine();
                if (message == null ) {
                    in.close(); out.close();
                    break;
                }
                System.out.println(username + ": " + message);

                if(message.equals("/end")){
                    out.println(message); in.close();out.close();
                    break;}

                out.println(username + ": " + message);
            }
        } catch (IOException e) {
            System.err.println("Error communicating with client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
