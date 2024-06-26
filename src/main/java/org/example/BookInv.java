package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;

import DB.DatabaseConnection;

public class BookInv {
    private DatabaseConnection db=new DatabaseConnection();

    // TODO uncomment this
    public List<String> format(List<String> booksList ){
        List<String> res = new ArrayList<>();
        int i=1;
        for(String s: booksList){
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
           // String book = ("////////////////////////////////////////////");
            String book = (i++)+("- Title: "+jsonObject.get("title").getAsString());
            book+=(" Author: "+jsonObject.get("author").getAsString());
            book+=(" Price: "+jsonObject.get("price").getAsString());
            JsonArray jsonArray = jsonObject.getAsJsonArray("genres");
            book+=(" Genres: [ ");
            for (JsonElement element : jsonArray) {
                book+=(element.getAsString()+' ');
            }
            book+="]";
            book+=(" Owner: "+jsonObject.get("owner").getAsString());
            res.add(book);
        }
        return res;
    }
    public List<String> browse(){
        List<String> booksList = db.getAllBooks();
        List<String> res = format(booksList);
        return res;
    }



    public List<String> search(String res[]) throws Exception{
        if(res.length != 2)
            throw new Exception("Invalid Input.Try again");
        String category=res[0]; String name=res[1];
        List<String> booksDoc = db.searchBooksBy(category,name);
        List<String> resultList = format(booksDoc);
        return resultList;
    }

     
    public void add(String username,String[] input) throws Exception{
        if(input.length != 6)
            throw new Exception("Invalid Input.Try again");

        String title=input[0],author=input[1],  description=input[2];
        String genres[] = input[3].split(" ");
        float price=Float.parseFloat(input[4]);
        int quantity = Integer.parseInt(input[5]);
        boolean insertion = db.insertBook(title,  author,  description, genres,
         username,  price,  quantity);
        if(!insertion)
            throw new Exception("Book already exists in user's library");
        }

    public void remove(String username ,String res[]) throws Exception{
        if(res.length != 1)
            throw new Exception("Invalid Input.Try again");
        String booktitle = res[0];
        if(!db.doesBookExist(username,booktitle))
            throw new Exception("Book does not exist");
        
        synchronized(this){db.removeBook(username,booktitle);}
    }

    
}
