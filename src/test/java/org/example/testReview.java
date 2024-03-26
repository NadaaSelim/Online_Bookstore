package org.example;

import DB.DatabaseConnection;
import junit.framework.TestCase;

public class testReview extends TestCase {
    public void testAddReview() throws Exception {
        // Create an instance of the Review class
       // Review review = new Review("good paper quality",5,"lama");

        // Call the addReview method and assert its result
       // assertTrue(review.addReview("Norwegian Wood","nadaselim",new DatabaseConnection()));

        Review review2 = new Review("book doesn't have a cover",1,"lama");
        assertTrue(review2.addReview("Kafka on the shore","nadaselim",new DatabaseConnection()));


    }
    public void testdisplay(){
       // DisplayBooks ui = new DisplayBooks();
        //ui.displayBooksWithRating(new DatabaseConnection());
    }
}
