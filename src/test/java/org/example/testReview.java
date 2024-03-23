package org.example;

import DB.DatabaseConnection;
import junit.framework.TestCase;

public class testReview extends TestCase {
    public void testAddReview() {
        // Create an instance of the Review class
        Review review = new Review("good paper quality",5,"lama");

        // Call the addReview method and assert its result
        assertTrue(review.addReview("The Stranger","nadaselim",new DatabaseConnection()));
    }
    public void testdisplay(){
        DisplayBooks ui = new DisplayBooks();
        ui.displayBooksWithRating(new DatabaseConnection());
    }
}
