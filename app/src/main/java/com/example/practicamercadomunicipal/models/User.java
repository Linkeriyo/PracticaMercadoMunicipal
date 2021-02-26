package com.example.practicamercadomunicipal.models;

import java.util.List;

public class User {

    public boolean admin;
    public String userID, name, image, email;
    public List<Invoice> invoice;
    public double balance;

    public User(boolean admin, String userID, String name, String image, String email, List<Invoice> invoice, double balance) {
        this.admin = admin;
        this.userID = userID;
        this.name = name;
        this.image = image;
        this.email = email;
        this.invoice = invoice;
        this.balance = balance;
    }
}
