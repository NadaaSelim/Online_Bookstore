package org.example;

import DB.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
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

    //TODO remove NaN??
    public List<String> displayBooksWithRating(DatabaseConnection dbc){

        MongoCollection<Document> bookscollection = dbc.getDatabase().getCollection("books");

        List<Document> books = bookscollection.find().into(new ArrayList<>());

        List<String> booksList = new ArrayList<>();

        booksList = sortByrating(books);

        dbc.close();
        return booksList;

    }

    public String Book(Document book){


        String display ="Title: "+book.getString("title")
                +"\nAuthor: "+book.getString("author")
                +"\nDescription: "+book.getString("description")
                +"\nGenres: "+book.getList("genres",String.class)
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

    //TODO display by genre listed by their current review
    public List<String>  displayByGenre(DatabaseConnection dbc , String genre) throws Exception {

        MongoCollection<Document> books = dbc.getDatabase().getCollection("books");

        List<Document> results = books.find(Filters.elemMatch("genres", Filters.eq("$eq", genre)))
                .projection(Projections.excludeId()).into(new ArrayList<>());

        List<String> booksList;
        if (results != null) {

            booksList = sortByrating(results);
        } else {
            throw new Exception("This genre does not exist");
        }

        dbc.close();
        return booksList;

    }

    public List<String> sortByrating(List<Document> books){

        List<String> booksList = new ArrayList<>();
        Map<Document,Double> bookRatings = new HashMap<>();


        for(Document book:books){
            bookRatings.put(book,overallRating(book));

            System.out.println(overallRating(book));
        }


        List<Map.Entry<Document, Double>> sortedBooks= new ArrayList<>(bookRatings.entrySet());
        sortedBooks.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        int count = 1;
        for (Map.Entry<Document, Double> entry : sortedBooks) {

            booksList.add("("+count+") \nOverall Rating: " + entry.getValue()+"\n\n"+Book(entry.getKey()) );
            count++;

        }
        return booksList;
    }


    //TODO display reviews for a book and overall  rating
    public String displayReviews(DatabaseConnection dbc ,String title,String owner) throws Exception{

        MongoCollection<Document> books = dbc.getDatabase().getCollection("books");
        Document result =  books.find(Filters.and(
                Filters.eq("title", title),
                Filters.eq("owner", owner))).first();


        String display = null;
        if (result != null) {
            display = Book(result);

        }else{
            throw new Exception("Book does not exist");
        }


        dbc.close();

        return display;


    }


}
