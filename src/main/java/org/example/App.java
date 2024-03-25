package org.example;

import DB.DatabaseConnection;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

public class App 
{
    public static void main( String[] args )
    {
        DatabaseConnection dbCon = new DatabaseConnection();




        /*
        dbCon.insertUser("Nada", "nadaselim", "123456");
        dbCon.insertUser("Jana", "janaselim", "123456");

        if(dbCon.doesUserExist("nadaselim")){
            Document doc =  dbCon.findUser("nadaselim","123456");
            System.out.println(doc.toJson());
        }
         genres = new String[]{"Fiction", "Philosophy"};
        dbCon.insertBook("The Stranger", "Albert Camus",
                "The first of Camus's novels published in his lifetime, the story follows Meursault, an indifferent settler in French Algeria, who, weeks after his mother's funeral, kills an unnamed Arab man in Algiers"
                ,genres, "nadaselim", 120,1);

         */
         String [] genres = new String[]{"Fiction", "Magical Realism"};
        dbCon.insertBook("Kafka on the shore", "Haruki Murakami",
                "Kafka on the Shore (2002) is a novel written by best-selling Japanese author Haruki Murakami. It follows the interwoven story of two eccentric characters: a teenage boy who runs away from home to escape his father, and an elderly man who has the ability to talk with cats."
                ,genres, "nadaselim", 150,0);


        genres = new String[]{"Fiction", "Romance"};
        dbCon.insertBook("Norwegian Wood", "Haruki Murakami",
                "Norwegian Wood by Haruki Murakami is a story set in late-1960s Japan, detailing the romantic exploits of Toru Watanabe, in particular his relationship with two women, Naoko and Midori Kobayashi."
                ,genres, "nadaselim",140,2);
/*
        List<String> booksList = dbCon.getAllBooks();
        for(String s: booksList){
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
            System.out.println("////////////////////////////////////////////");
            System.out.println("Title: "+jsonObject.get("title").getAsString());
            System.out.println("Author: "+jsonObject.get("author").getAsString());
            System.out.println("Price: "+jsonObject.get("price").getAsString());
            JsonArray jsonArray = jsonObject.getAsJsonArray("genres");
            System.out.println("Genres: ");
            for (JsonElement element : jsonArray) {
                System.out.println(element.getAsString());
            }
            System.out.println();
        }






        System.out.println(dbCon.doesBookExist("nadaselim","Kafka on the shore"));
        System.out.println(dbCon.isBookAvailable("nadaselim","Kafka on the shore"));
        System.out.println(dbCon.insertRequest("janaselim","nadaselim", "The Stranger"));

 */










    }
}
