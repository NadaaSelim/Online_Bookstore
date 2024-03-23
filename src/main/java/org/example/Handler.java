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
    
    List<String> startMsg = List.of("Register or login using the following formats",
    "register,[name],[user],[pass]","login,[user],[pass]");

    
    // Add rest of commands here
    List<String> formatStandard = List.of("Menu of commands with formats",
            "1- browse","2- search,[title/author/genre],[name]",
            "3- add,[title],[author],[desc],[genre1 genre2],[price],[quantity]",
            "4- remove,title");


    public Handler(Socket clientSocket, BufferedReader in, BufferedWriter out) {
        this.clientSocket = clientSocket;
        this.in = in; userAuth=new UserAuth(); bookInv=new BookInv();
        this.out = out;
    }

    public void writeToClient(List<String> outputToClient) throws IOException{
        for (String str : outputToClient) {
            out.write(str);
            out.newLine();
        }
        out.newLine(); 
                    out.flush();

    }
    @Override
    //TODO validate client input!!
    public void run() {
        // TODO Auto-generated method stub
        try {        
        writeToClient(startMsg);
        
    }
        catch(IOException e){e.printStackTrace();}
        String line = "";
        while(this.username==(null)){
            try {
                System.out.println("Inside run method");
                line=in.readLine();System.out.println("Client input: "+line);
                String res[] = line.split(",");
                String message="";
                if(res[0].equals("register")){
                    userAuth.register(res[1], res[2], res[3]);
                    this.username=res[2];
                    message=("Registration successful");}
                else if(res[0].equals("login")){
                    userAuth.login(res[1],res[2]);this.username=res[1];
                    message="Login successful";
                    }
                if(!message.equals("")){
                    List<String> stringList = new ArrayList<String>();
                    stringList.add(message);
                    stringList.addAll(formatStandard);
                   // stringList.addAll(formatStandard);
                    writeToClient(stringList);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                try {
                    writeToClient(List.of(e.getMessage()));
                    //out.write(e.getMessage()); out.newLine();   out.flush();

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
                    List<String> res = handleinput(line);
                    writeToClient(res);
                    // for(String []row:res){
                    //     String temp="";
                    //     for(String elem:row){ temp+=(elem+",");}
                    //     out.write(temp); out.newLine();out.flush();
                    // }
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
    
        public List<String> handleinput(String input) {
            String inputParsed[]= input.split(",");
            String[] res = Arrays.copyOfRange(inputParsed, 1, inputParsed.length);

            System.out.println(Arrays.toString(res)+'\t'+Arrays.toString(inputParsed));            
            try{
                switch(inputParsed[0]){

                // TODO uncomment this
                case "browse": {return bookInv.browse();}
                case "search" : return bookInv.search(res);
                case "add":  bookInv.add(this.username,res); return List.of("Book Added");
                case "remove": bookInv.remove(this.username,res[0]); return List.of("Book Removed");

            }
        }catch(Exception e){                    
            try {
                writeToClient(List.of(e.getMessage()));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
            return null;
        }
}
