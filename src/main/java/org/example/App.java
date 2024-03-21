package org.example;

import DB.DatabaseConnection;
import org.bson.Document;

public class App 
{
    public static void main( String[] args )
    {
        DatabaseConnection dbCon = new DatabaseConnection();

        dbCon.insertUser("Nada", "nadaselim", "123456");
        if(dbCon.doesUserExist("nadaselim")){
            Document doc =  dbCon.findUser("nadaselim","123456");
            System.out.println(doc.toJson());
        }
    }
}
