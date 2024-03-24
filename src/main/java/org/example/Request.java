package org.example;

import DB.DatabaseConnection;
import org.bson.types.ObjectId;

public class Request {

    private ObjectId id;
    private String title;
    private String lender;
    private String borrower;
    private int status;


    public Request(String title, String lender, String borrower) {
        this.title = title;
        this.lender = lender;
        this.borrower = borrower;
    }

    public ObjectId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLender() {
        return lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean addToDB(){
        DatabaseConnection dbCon = new DatabaseConnection();
        boolean res =  dbCon.insertRequest(this.borrower,this.lender,this.title);
        dbCon.close();
        return res;

    }


    public boolean accept(){
        DatabaseConnection dbCon = new DatabaseConnection();
        boolean res =  dbCon.acceptRequest(this.borrower,this.lender,this.title);
        dbCon.close();
        return res;
    }

    public boolean reject(){
        DatabaseConnection dbCon = new DatabaseConnection();
        boolean res =  dbCon.rejectRequest(this.borrower,this.lender,this.title);
        dbCon.close();
        return res;
    }


}
