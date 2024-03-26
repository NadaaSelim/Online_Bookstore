package org.example;

import java.net.*;
import java.util.List;
import java.io.*;
import java.util.Scanner;

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

         readListFromServer();
                // for (String str : serverResponse) {
                //     System.out.println(str);
                // }

        String line="";
        while (!line.equals("/end"))
        {
            try
            {
                
                
                line = input.readLine();
                if(line.equals("message")){this.message();}
                out.write(line); out.newLine();out.flush();
                readListFromServer();
                
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

    private void readListFromServer() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = serverInput.readLine()) != null) {
            if (line.isEmpty()||line.isBlank()) {
                break; // Exit the loop if the end of data is signaled
            }
    
        System.out.println(line);

            //sb.append(line).append(System.lineSeparator());
        }
       // return List.of(data.split("\\r?\\n"));
    }

    public void message() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("[your username],[friend's username]");
        String username = scanner.nextLine();

        Socket clientSocket = new Socket("127.0.0.1", 5001);


        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


        out.println(username);


        new Thread(() -> {
            while (true) {
                String message = scanner.nextLine();
                out.println(message);
                if(message.equals("/end")){
                    try{in.close();out.close(); break;
                    }catch(Exception e){e.printStackTrace();}

                }
            }
        }).start();

        while (true) {
            String message = in.readLine();
            if (message == null ) {
                break;
            }

            System.out.println(message);
            if(message.equals("/end")){
                try{in.close();out.close(); break;
                }catch(Exception e){e.printStackTrace();}

            }
        }

        clientSocket.close();
    }


    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.1",5000);

    }
}
