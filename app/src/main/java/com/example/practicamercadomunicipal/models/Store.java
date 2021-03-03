package com.example.practicamercadomunicipal.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Store {

    public String ID, name, image, imgStorage;
    public List<Product> products;

    public Store() {
        products = new ArrayList<>();
    }

    public Store(String ID, String name, String image, String imgStorage) {
        this.ID = ID;
        this.name = name;
        this.image = image;
        this.imgStorage = imgStorage;
        products = new ArrayList<>();
    }

    public Store(String ID, String name, Uri image, Uri imgStorage) {
        this.ID = ID;
        this.name = name;
        this.image = image.toString();
        this.imgStorage = imgStorage.toString();
        products = new ArrayList<>();
    }

    public void createOrOverrideProduct(Product newProduct) {
        Product currentProduct = null;
        for (Product product : products) {
            if (product.ID.equals(newProduct.ID)) {
                currentProduct = product;
            }
        }
        if (currentProduct != null) {
            products.remove(currentProduct);
        }

        products.add(newProduct);
    }
}
