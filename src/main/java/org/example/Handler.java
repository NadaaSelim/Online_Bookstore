package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Handler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private UserAuth userAuth;
    private BookInv bookInv;
    // TODO class user??
    private String username=null;

    //hardcoded data

    public Handler(Socket clientSocket, BufferedReader in, BufferedWriter out) {
        this.clientSocket = clientSocket;
        this.in = in; userAuth=new UserAuth(); bookInv=new BookInv();
        this.out = out;
    }
    @Override
    //TODO validate client input!!
    public void run() {
        // TODO Auto-generated method stub
        try {        out.write("Register or login using the following formats\n");
        out.write("register [name] [user] [pass]\n");
        out.write("login [user] [pass]\n");
        out.newLine();
        out.flush();

    }
        catch(IOException e){e.printStackTrace();}
        String line = "";
        while(this.username==(null)){
            try {
                System.out.println("Inside run method");
                line=in.readLine();System.out.println("Client input: "+line);
                String res[] = line.split(" ");
                if(res[0].equals("register")){
                    userAuth.register(res[1], res[2], res[3]);
                    this.username=res[2];
                    out.write("Registration successful\n");out.flush();}
                else if(res[0].equals("login")){
                    userAuth.login(res[1],res[2]);this.username=res[1];
                out.write("Login Successful\n");out.flush();}
                

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                try {
                    out.write(e.getMessage()); out.newLine();   out.flush();

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
        
        // reads message from client until "Over" is sent
        while (!line.equals("Over")) {
            try {
                    line = in.readLine();
                    System.out.println(line);
                    List<String[]> res = handleinput(line);
                    for(String []row:res){
                        String temp="";
                        for(String elem:row){ temp+=(elem+",");}
                        out.write(temp); out.newLine();out.flush();
                    }
            } catch (IOException i) {
                    System.out.println(i);
            }catch(Exception e){e.printStackTrace();}

        }
                
        System.out.println("Closing connection");

        try{
                clientSocket.close();
                 in.close();} 
        catch(IOException e){e.printStackTrace();}   
        }
    
        public List<String[]> handleinput(String input) throws Exception{
            String res[]= input.split(" ");
            switch(input){
                // TODO uncomment this
                //case "browse": {return bookInv.browse();}
                //case "search" : return bookInv.search(res);
                case "add":  bookInv.add(res);out.write("Book Added\n");out.flush();
                case "remove": bookInv.remove(res); out.write("Book Removed\n");out.flush();

            }
            return null;
        }
}
