package org.example;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class App 
{
    public static void main( String[] args )
    {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("student");

        for (String name : database.listCollectionNames()) {
            System.out.println(name);
        }
    }
}
