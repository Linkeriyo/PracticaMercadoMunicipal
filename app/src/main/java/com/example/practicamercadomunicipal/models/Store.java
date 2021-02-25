package com.example.practicamercadomunicipal.models;

import java.util.ArrayList;
import java.util.List;

public class Store {

    public String ID, name, image;
    public List<Product> products;

    public Store() {
        products = new ArrayList<>();
    }

    public Store(String ID, String name, String image) {
        this.ID = ID;
        this.name = name;
        this.image = image;
        products = new ArrayList<>();
    }
}
