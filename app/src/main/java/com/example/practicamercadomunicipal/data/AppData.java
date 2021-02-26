package com.example.practicamercadomunicipal.data;

import com.example.practicamercadomunicipal.models.Store;

import java.util.List;

public class AppData {
    //Lista de locales a la cual se puede acceder desde cualquier parte del programa.
    public static List<Store> storeList;

    public static Store getStoreById(String storeID) {
        for (Store store : storeList) {
            if (store.ID.equals(storeID)) {
                return store;
            }
        }
        return null;
    }
}
