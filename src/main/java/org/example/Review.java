package org.example;

import DB.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class Review  implements Serializable {


    //TODO all crud operations 1-add a review 2-retrieve reviews for a certain book 3-retrieve rating for statics
    private ObjectId id;
    private  String username;
    private String text;
    private int rating;         // from 0 till 1

    public Review() {


    }

    public Review(ObjectId id,String username,String text,int rating) {
        this.id = id;
        this.text = text;
        this.rating = rating;
        this.username = username;

    }

    public Review(String text , int rating, String username) {
        this.id = new ObjectId();
        this.text = text;
        this.rating = rating;
        this.username = username;
    }

    public Review (Document review){

        this.id =review.getObjectId("_id");
        this.username = review.getString("username");
        this.text = review.getString("review_text");
        this.rating =review.getInteger("rating");


    }



    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString(){


        return "Username: "+username+"\n"
                +"Review: "+text+"\n"
                +"Rating: "+rating+"\n";

    }



    // TODO check rating is is in limit (handle exception)
    public boolean validRating(int rating) {
        return rating >= 1 && rating <= 5;
    }

    //TODO handle request bas fe abl m3 call function addReview
    public boolean addReview(String title , String owner,DatabaseConnection dbc) throws  Exception{

        // 1- check rating
        if(!validRating(this.rating)){
            throw new Exception("Cannot add review Rating out of Range [1-5]");
        }

        MongoCollection<Document> reviews = dbc.getDatabase().getCollection("reviews");
        Document review = new Document("_id", this.id);
        review.append("review_text", this.text)
                .append("rating",this.rating)
                .append("username",this.username);
       // reviews.insertOne(review);
        Document query = new Document("title", title).append("owner", owner);

        MongoCollection<Document> books = dbc.getDatabase().getCollection("books");
        Document book = books.find(new Document("title",title).append("owner",owner)).first();
        List<Document> reviewsl = (List<Document>) book.get("reviews");
        reviewsl.add(review);
        synchronized(this){books.replaceOne(query, book, new ReplaceOptions().upsert(true));}


        dbc.close();
        return true;

    }
}
