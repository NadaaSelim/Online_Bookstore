package org.example;
import DB.DatabaseConnection;
import org.bson.Document;

public class UserAuth {
    private DatabaseConnection db=new DatabaseConnection();
    

    public void login(String username,String pass) throws Exception{
        Document user = db.findUser(username,pass);
        if(!db.doesUserExist(username))
            throw new Exception("404 Username not found");
        if(user==null)
            throw new Exception("401 Wrong Password");

        //return user.getString("password");
    //     Docu index = db.doesUserExist(username);
    //     if(index==-1){
    //         throw new Exception("404 Username not found");}
    //     if(!usersdata.get(index)[1].equals(pass))
    //         throw new Exception("401 Wrong Password");
        
    //     //when no exception thrown the login is successful and the thread's username is assigned
    //     this.userID = username;
    //    // out.write("Login Complete");
    //     System.out.println("Login Complete");
    }

    public void register(String name,String username,String pass) throws Exception{
        if(db.doesUserExist(username)){
            throw new Exception("User already exists");
        }
        synchronized(this){db.insertUser(name, username, pass);}
        //out.write("Register Complete");
        //return username;
    }


}
