package com.example.practicamercadomunicipal.data;


import android.net.Uri;

import com.example.practicamercadomunicipal.models.Invoice;
import com.example.practicamercadomunicipal.models.Product;
import com.example.practicamercadomunicipal.models.Store;
import com.example.practicamercadomunicipal.models.User;

import java.util.List;

public class AppData {
    //Listas a las cuales se puede acceder desde cualquier parte del programa.
    public static List<Store> storeList;
    //Lista de usuarios
    public static List<User> userList;
    //Lista de facturas del usuario
    public static List<Invoice> invoiceList;

    public static Product defaultProduct = new Product(
            "",
            "defaultProduct",
            "Producto Eliminado",
            0,
            Uri.EMPTY,
            Uri.EMPTY,
            0,
            false
    );

    public static Store getStoreById(String storeID) {
        for (Store store : storeList) {
            if (store.ID.equals(storeID)) {
                return store;
            }
        }
        return null;
    }

    public static Product getProductById(String storeID, String productID) {
        Store store = getStoreById(storeID);
        if (store != null) {
            for (Product product : store.products) {
                if (product.ID.equals(productID)) {
                    return product;
                }
            }
        }
        return defaultProduct;
    }

    public static User getUserById(String uid) {
        for (User user : userList) {
            if (user.userID.equals(uid)) {
                return user;
            }
        }
        return null;
    }

    public static Invoice getInvoiceByNumber(int num) {
        for (Invoice invoice : invoiceList) {
            if (invoice.number == num) {
                return invoice;
            }
        }
        return null;
    }

    public static int getNextInvoiceNumber() {
        int maxnumber = 0;
        for (Invoice invoice : invoiceList) {
            if (invoice.number > maxnumber) {
                maxnumber = invoice.number;
            }
        }
        return maxnumber + 1;
    }
}
