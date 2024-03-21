package DB;


import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.List;

import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;

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

    public List<Document> getAllBooks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllBooks'");
    }

    public void addBook(String username, String title, String author, String genre, float price, int quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addBook'");
    }

    public void removeBook(String username, String booktitle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeBook'");
    }

    public boolean doesBookExist(String username, String booktitle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'doesBookExist'");
    }

    public List<Document> search(String category, String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    //TODO update friends list



}
