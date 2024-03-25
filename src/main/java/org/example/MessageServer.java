package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5001); // Choose a port number

        System.out.println("Message Server started on port 5001");

        while (true) {
            // Accept connections from clients
            Socket client1 = serverSocket.accept();
            Socket client2 = serverSocket.accept();

            System.out.println("Client 1 connected from: " + client1.getInetAddress());
            System.out.println("Client 2 connected from: " + client2.getInetAddress());

            // Create threads to handle each client communication
            ClientHandler handler1 = new ClientHandler(client1, client2);
            ClientHandler handler2 = new ClientHandler(client2, client1);

            handler1.start();
            handler2.start();
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private Socket peerSocket;

    public ClientHandler(Socket clientSocket, Socket peerSocket) {
        this.clientSocket = clientSocket;
        this.peerSocket = peerSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(peerSocket.getOutputStream(), true);

            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }

                System.out.println( + clientSocket.getPort() + ": " + message);
                out.println("Client " + clientSocket.getPort() + ": " + message);
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
