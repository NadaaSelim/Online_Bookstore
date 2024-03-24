package org.example;

import DB.DatabaseConnection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.print.Doc;
import java.util.*;

public class DisplayBooks {


    public double overallRating(Document book){

        List<Document> reviews = (List<Document>) book.get("reviews");

        double totalRating = 0.0;
        for (Document review : reviews) {

            Review r = new Review(review);
            //System.out.println(r);
            totalRating += r.getRating();

        }
        return totalRating/ reviews.size();
    }


    /*ToDo
       1- calculate for  rating for all books
            a-a7ot kol book fe list of strings
            b-a7ot rating
       2- sort them according to rating

     */
    public List<String> displayBooksWithRating(DatabaseConnection dbc){

        MongoCollection<Document> books = dbc.getDatabase().getCollection("books");
        MongoCursor<Document> cursor = books.find().iterator();
        List<String> booksList = new ArrayList<>();
        Map<Document,Double> bookRatings = new HashMap<>();


        while (cursor.hasNext()) {

            Document book = cursor.next();

            bookRatings.put(book,overallRating(book));

            System.out.println(overallRating(book));

        }
        List<Map.Entry<Document, Double>> sortedBooks= new ArrayList<>(bookRatings.entrySet());
        sortedBooks.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));

        for (Map.Entry<Document, Double> entry : sortedBooks) {

            booksList.add("Overall Rating: " + entry.getValue()+"\n\n"+Book(entry.getKey()) );


        }
        // Close the cursor and MongoDB connection
        cursor.close();
        dbc.close();
        return booksList;

    }

    public String Book(Document book){


        String display ="Title: "+book.getString("title")
                +"\nAuthor: "+book.getString("author")
                +"\nOwner: "+book.getString("owner")
                +"\nPrice: "+book.getDouble("price")
                +"\nQuantity: "+book.getInteger("quantity")
                +"\nReviews:\n\n";


        List<String> reviews = ReviewsofBook(book);
        for(String review : reviews){
            display += review;
            display += "\n";
        }

        return display;

    }

    public List<String> ReviewsofBook(Document book){

        List<Document> reviewsDoc = (List<Document>) book.get("reviews");
        List<String> reviews= new ArrayList<>();


        for (Document review : reviewsDoc) {

            Review r = new Review(review);
            reviews.add(r.toString());


        }
        return reviews;
    }

    //TODO display by genre
    public void  displayByGenre(DatabaseConnection dbc ,String genre){

    }
    //ToDO display by mostly borrowed from mesh lzm
    //TODO display reviews for a book and overall  rating

    //TODO handler
}
