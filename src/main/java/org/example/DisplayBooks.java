package org.example;

import DB.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.List;

public class DisplayBooks {
    public double overallRating(Document book){

        List<Document> reviews = (List<Document>) book.get("reviews");

        double totalRating = 0.0;
        for (Document review : reviews) {

            totalRating += review.getInteger("rating");
        }
        return totalRating/ reviews.size();
    }

    public void displayBooksWithRating(DatabaseConnection dbc){

        MongoCollection<Document> books = dbc.getDatabase().getCollection("books");
        MongoCursor<Document> cursor = books.find().iterator();

        while (cursor.hasNext()) {

            Document book = cursor.next();
            overallRating(book);
            System.out.println(overallRating(book));


        }

        // Close the cursor and MongoDB connection
        cursor.close();
        dbc.close();

    }

    //TODO display by genre
    //ToDO display by mostly borrowed from
}
