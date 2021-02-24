package com.example.practicamercadomunicipal.models;

import java.util.ArrayList;
import java.util.List;

public class Store {

    String ID, name, image;
    List<Product> products;

    public Store() {
        products = new ArrayList<>();
    }

}
