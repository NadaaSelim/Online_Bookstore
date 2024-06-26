package DB;


import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
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
                .append("password", password)
                .append("friends", Arrays.asList()));

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


    public List<String> getFriends(String username) {
        MongoCollection<Document> collection = this.database.getCollection("users");
        Document doc = collection.find(eq("username", username)).first();
        List<String> friends = (List<String>) doc.get("friends");
        return friends;
    }

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
                                String owner, float price, int quantity){

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
                    .append("price", price)
                    .append("quantity", quantity)
                    .append("reviews", Arrays.asList()));
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

    public int bookQuantity(String owner, String title) {
        MongoCollection<Document> collection = this.database.getCollection("books");
        Document doc = collection.find(and(eq("owner", owner),eq("title", title))).first();
        return doc.getInteger("quantity");
    }

    public boolean isBookAvailable(String username, String title){
        MongoCollection<Document> collection = this.database.getCollection("books");

        Bson projectionFields = Projections.fields(Projections.include("quantity"),Projections.excludeId());
        Document doc = collection.find(and(eq("owner", username),eq("title", title)))
                .projection(projectionFields).first();

        boolean res;
        if(doc == null){
            res = false;
        }
        else{
            int count = doc.getInteger("quantity");
            if(count > 0){
                res = true;
            }
            else{
                res = false;
            }
        }
        return res;

    }

    public boolean decrementQuantity(String username, String title){
        MongoCollection<Document> collection = this.database.getCollection("books");
        Document query = new Document().append("title",title).append("owner",username);

        int count = this.bookQuantity(username,title);

        Bson updates = Updates.set("quantity", count-1);

        UpdateResult result = collection.updateOne(query, updates);

        boolean res;
        if(result.getModifiedCount() == 1){
            res = true;
        }
        else{
            res = false;
        }

        return res;
    }

    public boolean incrementQuantity(String username, String title){
        MongoCollection<Document> collection = this.database.getCollection("books");
        Document query = new Document().append("title",title).append("owner",username);

        int count = this.bookQuantity(username,title);

        Bson updates = Updates.set("quantity", count+1);

        UpdateResult result = collection.updateOne(query, updates);

        boolean res;
        if(result.getModifiedCount() == 1){
            res = true;
        }
        else{
            res = false;
        }

        return res;
    }

    // TODO change for genre
    public List<String> searchBooksBy(String key, String value) {
        MongoCollection<Document> collection = this.database.getCollection("books");
        Bson projectionFields = Projections.fields(Projections.excludeId());
        FindIterable<Document> docs = collection.find(eq(key,value)).projection(projectionFields);
        MongoCursor<Document> cursor =  docs.iterator();
        List<String> list = new ArrayList<String>();

        while(cursor.hasNext())
            list.add(cursor.next().toJson());
        return list;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
    public MongoDatabase getDatabase() {
        return database;
    }

    /////////////////////////   CRUD Operations for Request    /////////////////////////

    private boolean doesRequestExist(String borrowerUsername, String lenderUsername, String title){
        MongoCollection<Document> collection = this.database.getCollection("requests");
        Document doc = collection.find(and(eq("lender", lenderUsername),eq("borrower", borrowerUsername),
                eq("title",title))).first();
        boolean res;
        if(doc == null){
            res = false;
        }
        else {
            res = true;
        }
        return res;
    }

    /*
        the status of a request is:
        1 : accepted
        0 : pending
        -1 : rejected
     */

    public boolean insertRequest(String borrowerUsername, String lenderUsername, String title){
        boolean res;
        if(this.doesUserExist(borrowerUsername) && this.doesBookExist(lenderUsername,title)
        && this.isBookAvailable(lenderUsername, title) && !this.doesRequestExist(borrowerUsername,lenderUsername,title)
        && !lenderUsername.equals(borrowerUsername)){
            MongoCollection<Document> collection = this.database.getCollection("requests");
            InsertOneResult result = collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("title", title)
                    .append("lender", lenderUsername)
                    .append("borrower", borrowerUsername)
                    .append("status", 0));
            res = true;
        }
        else{
            res = false;
        }

        return(res);
    }



    public boolean acceptRequest(String borrowerUsername, String lenderUsername, String title){
        MongoCollection<Document> collection = this.database.getCollection("requests");
        Document query = new Document().append("title",title).append("lender",lenderUsername)
                .append("borrower",borrowerUsername);

        Bson updates = Updates.set("status", 1);
        MongoCollection<Document> users = this.database.getCollection("users");
        Document lender = users.find(eq("username",lenderUsername)).first();
        Document borrower = users.find(eq("username",borrowerUsername)).first();
        List<String> lenderFriends = (List<String>) lender.get("friends");
        List<String> borrowerFriends = (List<String>) borrower.get("friends");
        if(!lenderFriends.contains(borrowerUsername)){
            lenderFriends.add(borrowerUsername);
            borrowerFriends.add(lenderUsername);
        }
        Bson lenderUpdates = Updates.set("friends", lenderFriends);
        Bson borrowerUpdates = Updates.set("friends", borrowerFriends);
        users.updateOne(eq("username",lenderUsername),lenderUpdates);
        users.updateOne(eq("username",borrowerUsername),borrowerUpdates);
        UpdateResult result = collection.updateOne(query, updates);
        this.decrementQuantity(lenderUsername,title);
        boolean res;
        if(result.getModifiedCount() == 1){
            res = true;
        }
        else{
            res = false;
        }

        return res;

    }


    public boolean rejectRequest(String borrowerUsername, String lenderUsername, String title){
        MongoCollection<Document> collection = this.database.getCollection("requests");
        Document query = new Document().append("title",title).append("lender",lenderUsername)
                .append("borrower",borrowerUsername);

        Bson updates = Updates.set("status", -1);

        UpdateResult result = collection.updateOne(query, updates);
        this.incrementQuantity(lenderUsername,title);
        boolean res;
        if(result.getModifiedCount() == 1){
            res = true;
        }
        else{
            res = false;
        }

        return res;

    }

    // requests sent for my books
    public List<String> getAllRequestsForMe(String lender) {

        MongoCollection<Document> collection = this.database.getCollection("requests");
        Bson projectionFields = Projections.fields(Projections.excludeId());
        FindIterable<Document> docs = collection.find(eq("lender",lender)).projection(projectionFields);
        MongoCursor<Document> cursor =  docs.iterator();
        List<String> list = new ArrayList<String>();

        while(cursor.hasNext())
            list.add(cursor.next().toJson());
        return list;

    }


    // requests that I have sent
    public List<String> getMyRequests(String borrower) {

        MongoCollection<Document> collection = this.database.getCollection("requests");
        Bson projectionFields = Projections.fields(Projections.excludeId());
        FindIterable<Document> docs = collection.find(eq("borrower",borrower)).projection(projectionFields);
        MongoCursor<Document> cursor =  docs.iterator();
        List<String> list = new ArrayList<String>();

        while(cursor.hasNext())
            list.add(cursor.next().toJson());
        return list;

    }


    public long getBorrowedBooksCnt(){
        MongoCollection<Document> collection = database.getCollection("requests");
        Document query = new Document("status", 1);
        long count = collection.countDocuments(query);
        return count;
    }

    public long getAllBooksCnt(){
        MongoCollection<Document> collection = database.getCollection("books");
        Document groupStage = new Document("$group",
                new Document("_id", null)
                .append("totalQuantity", new Document("$sum", "$quantity")));
        
                Document result = collection.aggregate(Arrays.asList(groupStage)).first();

        return result.getInteger("totalQuantity", 0);

    }

    public List<Long> getAllReqsCnt(){
        MongoCollection<Document> collection = database.getCollection("requests");
        List<Long> res= new ArrayList<>();
        for(int i=1;i>=-1;i--){
            Document query = new Document("status", i);
            long count = collection.countDocuments(query);
            res.add(count);
        }
        return res;
    }


}
