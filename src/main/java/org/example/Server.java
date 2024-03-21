package org.example;

import java.io.*;
import java.net.*;


public class Server {

    private ServerSocket server = null;
    private Socket socket = null;
    private BufferedReader in =null;
    private BufferedWriter out = null;

    public Server(int port) {
        try {

            server = new ServerSocket(port);

            System.out.println("Server started");
            System.out.println("waiting for a client ....");

            while(true){
                socket = server.accept();

                System.out.println("client accepted");

                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out =  new BufferedWriter(new           
                         OutputStreamWriter(socket.getOutputStream()));
                Thread t = new Thread(new Handler(socket,in,out));
                t.start();
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        public static void main(String[] args) {
        Server server = new Server(5000);

    }
}
