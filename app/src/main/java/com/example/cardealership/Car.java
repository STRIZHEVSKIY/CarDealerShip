package com.example.cardealership;

import java.io.Serializable;

public class Car implements Serializable {
    private String model;
    private int year;
    private String price;
    private String imageUri; // Поле для хранения URI изображения

    // Конструктор
    public Car(String model, int year, String price, String imageUri) {
        this.model = model;
        this.year = year;
        this.price = price;
        this.imageUri = imageUri;
    }

    // Геттеры
    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String  getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUri;
    }
}
