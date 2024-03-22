package org.example;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;

import DB.DatabaseConnection;

public class BookInv {
    private DatabaseConnection db=new DatabaseConnection();

    // TODO uncomment this
    /*
    public List<String[]> browse(){
        List<Document> booksDoc = db.getAllBooks();
        List<String[]> resultList = new ArrayList<>();
        
        for (Document document : booksDoc) {
            resultList.add(document.values().toArray(new String[0]));
        }
        return resultList;
    }

     */

    public List<String[]> search(String res[]){
        String category=res[0]; String name=res[1];
        List<Document> booksDoc = db.search(category,name);
        List<String[]> resultList = new ArrayList<>();
        
        for (Document document : booksDoc) {
            resultList.add(document.values().toArray(new String[0]));
        }
        return resultList;
    }
    public void add(String[] input){
    
        String username=input[0];
        String title=input[1];
        String author=input[2];String genre=input[3];
        float price=Float.parseFloat(input[4]);
        int quantity = Integer.parseInt(input[5]);
        // TODO uncomment this
        //db.insertBook(username,title,author,genre,price,quantity);
    }

    public void remove(String res[]) throws Exception{
        String username=res[0], booktitle=res[1];

        if(db.doesBookExist(username,booktitle))
            throw new Exception("Book does not exist");
        
        synchronized(this){db.removeBook(username,booktitle);}
    }

    
}
