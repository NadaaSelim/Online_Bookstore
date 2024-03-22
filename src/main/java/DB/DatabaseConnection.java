package DB;


import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.DeleteResult;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonObject;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.client.result.InsertOneResult;

import java.util.Arrays;

public class DatabaseConnection {

    private MongoClient mongoClient;
    private ConnectionString connectionString;
    private MongoDatabase database;

    public DatabaseConnection(){
        this.connectionString = new ConnectionString("mongodb://localhost:27017");
        this.mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase("bookstore_db");
    }

    /////////////////////////   CRUD Operations for User    /////////////////////////

    //returns the new user's id or null if insertion failed
    public BsonValue insertUser(String name, String username, String password){
        MongoCollection<Document> collection = this.database.getCollection("users");
        // Inserts a new user
        InsertOneResult result = collection.insertOne(new Document()
                .append("_id", new ObjectId())
                .append("name", name)
                .append("username", username)
                .append("password", password));

        return(result.getInsertedId());
    }

    // returns true if user already exists and false if it doesn't
    public boolean doesUserExist(String username){
        MongoCollection<Document> collection = this.database.getCollection("users");
        Document doc = collection.find(eq("username", username)).first();
        boolean res;
        if(doc == null){
            res = false;
        }
        else {
            res = true;
        }
        return res;
    }

    // returns the user if it exists or null
    public Document findUser(String username, String password) {
        MongoCollection<Document> collection = this.database.getCollection("users");
        Document doc = collection.find(and(eq("username", username), eq("password", password))).first();
        return doc;
    }

    private ObjectId getUserId(String username) {
        MongoCollection<Document> collection = this.database.getCollection("users");
        Document doc = collection.find(eq("username", username)).first();
        ObjectId id = doc.getObjectId("_id");
        return id;
    }

    //TODO update friends list

    /////////////////////////   CRUD Operations for Book    /////////////////////////
    public List<String> getAllBooks() {

        MongoCollection<Document> collection = this.database.getCollection("books");
        Bson projectionFields = Projections.fields(Projections.excludeId());
        FindIterable<Document> docs = collection.find().projection(projectionFields);
        MongoCursor<Document> cursor =  docs.iterator();
        List<String> list = new ArrayList<String>();

        while(cursor.hasNext())
            list.add(cursor.next().toJson());
        return list;

    }

    public boolean insertBook(String title, String author, String description, String[] genres,
                                String owner, boolean available, float price, int quantity){

        // TODO validate that the book doesn't already exist for the user
        MongoCollection<Document> collection = this.database.getCollection("books");
        Document doc = collection.find(and(eq("owner",owner), eq("title",title))).first();
        boolean res;
        if(doc != null){
            res = false;
        }
        else {
            InsertOneResult result = collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("title", title)
                    .append("author", author)
                    .append("description", description)
                    .append("genres", Arrays.asList(genres))
                    .append("owner", owner)
                    .append("available", available)
                    .append("price", price)
                    .append("quantity", quantity));
            res = true;
        }

        return(res);
    }

    public boolean removeBook(String owner, String title) {
        MongoCollection<Document> collection = this.database.getCollection("books");
        Bson query = and(eq("owner", owner),eq("title", title));
        DeleteResult result = collection.deleteOne(query);

        boolean res;
        if(result.getDeletedCount() == 0){
            res = false;
        }
        else{
            res = true;
        }

        return res;

    }

    public boolean doesBookExist(String owner, String title) {
        MongoCollection<Document> collection = this.database.getCollection("books");
        Document doc = collection.find(and(eq("owner", owner),eq("title", title))).first();
        boolean res;
        if(doc == null){
            res = false;
        }
        else {
            res = true;
        }
        return res;
    }

    public boolean isBookAvailable(String username, String title){
        MongoCollection<Document> collection = this.database.getCollection("books");

        Bson projectionFields = Projections.fields(Projections.include("available"),Projections.excludeId());
        Document doc = collection.find(and(eq("owner", username),eq("title", title)))
                .projection(projectionFields).first();

        boolean res;
        if(doc == null){
            res = false;
        }
        else{
            String s = doc.get("available").toString();
            res = !s.equals("false");
        }
        return res;

    }

    //TODO search by any key and value pair
    public List<Document> search(String key, String value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }



}
