package org.example;

import java.net.*;
import java.io.*;

public class Client {
    // initialize socket and input output streams
    private Socket socket = null;
    private BufferedReader input = null,serverInput=null;
    private BufferedWriter out = null;


    public Client(String address,int port) throws IOException {
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");

            input =  new BufferedReader(new InputStreamReader(System.in));

            out = new BufferedWriter(new OutputStreamWriter( socket.getOutputStream()));
            serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String serverResponse;
                while ((serverResponse = serverInput.readLine()) != null) {
                    System.out.println("Server: " + serverResponse);
                    if (serverResponse.isEmpty()) {
                        break; // Exit loop when empty line is encountered
                    }
                }

        String line="";
        while (!line.equals("Over"))
        {
            try
            {
                
                    line = input.readLine();
                out.write(line); out.newLine();out.flush();
                System.out.println(serverInput.readLine());
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        }
        // close the connection
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.1",5000);

    }
}
