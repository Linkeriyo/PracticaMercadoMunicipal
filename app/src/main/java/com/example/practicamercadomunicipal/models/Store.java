package com.example.practicamercadomunicipal.models;

import java.util.ArrayList;
import java.util.List;

public class Store {

    public String ID, name, image;
    public List<Product> products;

    public Store() {
        products = new ArrayList<>();
    }
}
