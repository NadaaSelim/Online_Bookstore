package org.example;

import java.util.ArrayList;
import java.util.List;

import DB.DatabaseConnection;

public class AdminOps {
    static String pass="1234"; 
    private DatabaseConnection db = new DatabaseConnection();
    
    public void login(String pass) throws Exception{
        if(!pass.equals(AdminOps.pass))     
            throw new Exception("Wrong Password");   
    }

    public List<String> viewLibStats(){
        long borrowed = db.getBorrowedBooksCnt();
        long available = db.getAllBooksCnt() - borrowed;
        List<Long> reqList = db.getAllReqsCnt();
        List<String> res= new ArrayList<>();
        res.add("Number of Borrowed Books: "+borrowed);
        res.add("Number of Available Books: "+available);
        res.add("Number of Accepted Books:" + reqList.get(0));
        res.add("Number of Pending Books:" + reqList.get(1));
        res.add("Number of Rejected Books:" + reqList.get(2));
        return res;
    }
}
