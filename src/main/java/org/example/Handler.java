package org.example;

import DB.DatabaseConnection;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Handler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private UserAuth userAuth;
    private BookInv bookInv;
    private DisplayBooks display;

    // TODO class user??
    private String username=null;
    
    List<String> startMsg = List.of("Register or login using the following formats",
    "register,[name],[user],[pass]","login,[user],[pass]");

    
    // Add rest of commands here
    List<String> formatStandard = List.of("Menu of commands with formats",
            "1- browse","2- search,[title/author/genre],[name]",
            "3- add,[title],[author],[desc],[genre1 genre2],[price],[quantity]",
            "4- remove,title",
            "5- request,[title],[lender]",
            "6- show_requests",
            "7- accept,[title],[borrower]",
            "8- reject,[title],[borrower]",
            "9- my_requests",
            "10- display by rating",
            "11- display by genre,[genre]",
            "12- message",
            "13-add review,[title],[owner],[review],[rating]");


    public Handler(Socket clientSocket, BufferedReader in, BufferedWriter out) {
        this.clientSocket = clientSocket;
        this.in = in; userAuth=new UserAuth(); bookInv=new BookInv();this.display=new DisplayBooks();
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


        private List<String> formatRequest(List<String> booksList, boolean forMe){
            int i = 1;
            List<String> formatedList = new ArrayList<>();
            for(String s: booksList){
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
                String requ = "";
                requ = requ + i + "- Title: " + jsonObject.get("title").getAsString();
                if(forMe){
                    requ = requ + "   Borrower: " + jsonObject.get("borrower").getAsString();
                }
                else{
                    requ = requ + "   Lender: " + jsonObject.get("lender").getAsString();
                }

                int status = jsonObject.get("status").getAsInt();
                if(status == 0)
                    requ += "   Status: Pending";
                else if(status == 1)
                    requ += "   Status: Accepted";
                else if(status == -1)
                    requ += "   Status: Rejected";
                formatedList.add(requ);
                i++;
            }

            return formatedList;
        }

        private  boolean formatAccepted(List<String> booksList , String title,String owner){
            int i = 1;
            List<String> formatedList = new ArrayList<>();
            for(String s: booksList){
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(s, JsonObject.class);

                int status = jsonObject.get("status").getAsInt();
                String titleac = jsonObject.get("title").getAsString();
                String lender =jsonObject.get("lender").getAsString();
                if(status == 1 &&  titleac.equals(title) && lender.equals(owner)) {

                  return true;
                }



            }
            return false;

        }
    
        public List<String> handleinput(String input) {
            String inputParsed[]= input.split(",");
            String[] res = Arrays.copyOfRange(inputParsed, 1, inputParsed.length);

            System.out.println(Arrays.toString(res)+'\t'+Arrays.toString(inputParsed));

            try{
                switch(inputParsed[0]){

                // TODO add options
                    // TODO add case for /end
                    case "browse": {return bookInv.browse();}
                    case "search" : return bookInv.search(res);
                    case "add":  bookInv.add(this.username,res); return List.of("Book Added");
                    case "remove": bookInv.remove(this.username,res[0]); return List.of("Book Removed");
                    case "request":{
                        Request req = new Request(res[0],res[1],this.username);
                        if(req.addToDB())
                            return List.of("Request Added");
                        else
                            throw new RuntimeException("Error couldn't add your request");
                    }
                    case "show_requests":{
                        DatabaseConnection dbCon = new DatabaseConnection();
                        List<String> booksList = dbCon.getAllRequestsForMe(this.username);
                        dbCon.close();
                        List<String> formatedList = this.formatRequest(booksList, true);

                        return formatedList;

                    }
                    case "accept":{
                        Request req = new Request(res[0],this.username,res[1]);
                        if(req.accept()){
                            return List.of("Request Accepted");
                        }
                        else{
                            throw new RuntimeException("Error couldn't accept that request");
                        }
                    }
                    case "reject":{
                        Request req = new Request(res[0],this.username,res[1]);
                        if(req.reject()){
                            return List.of("Request Rejected");
                        }
                        else{
                            throw new RuntimeException("Error couldn't reject that request");
                        }
                    }
                    case "my_requests":{
                        DatabaseConnection dbCon = new DatabaseConnection();
                        List<String> booksList = dbCon.getMyRequests(this.username);
                        dbCon.close();
                        List<String> formatedList = this.formatRequest(booksList, false);

                        return formatedList;
                    }
                    case "display by rating":{
                        DatabaseConnection dbCon = new DatabaseConnection();
                        return display.displayBooksWithRating(dbCon);
                    }
                    case "display by genre":{
                        DatabaseConnection dbCon = new DatabaseConnection();
                        return display.displayByGenre(dbCon,res[0]);
                    }
                    case "message":{
                        return List.of("Messaging ended");
                    }
                    case "add review":{
                        DatabaseConnection dbCon = new DatabaseConnection();
                        List<String> booksList = dbCon.getMyRequests(this.username);

                        boolean canaddreview = formatAccepted(booksList,res[0],res[1]);
                        if(canaddreview){
                            Review review = new Review(res[2],Integer.parseInt(res[3]),this.username);
                            try{
                                review.addReview(res[0],res[1],dbCon);
                            }
                            catch (Exception e){
                                System.out.println(e);

                            }
                        }
                        else {
                            throw new Exception("Request not approved");
                        }

                        dbCon.close();




                    }


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
