package com.example.cardealership;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CarDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_CAR_MODEL = "com.example.cardealership.CAR_MODEL";
    public static final String EXTRA_CAR_YEAR = "com.example.cardealership.CAR_YEAR";
    public static final String EXTRA_CAR_PRICE = "com.example.cardealership.CAR_PRICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        // Получение данных из интента
        String carModel = getIntent().getStringExtra(EXTRA_CAR_MODEL);
        int carYear = getIntent().getIntExtra(EXTRA_CAR_YEAR, 0);
        String carPrice = getIntent().getStringExtra(EXTRA_CAR_PRICE);

        // Установка данных в TextView
        TextView modelTextView = findViewById(R.id.textViewCarModel);
        TextView yearTextView = findViewById(R.id.textViewCarYear);
        TextView priceTextView = findViewById(R.id.textViewCarPrice);

        modelTextView.setText(carModel);
        yearTextView.setText(String.valueOf("Год: " + carYear));
        priceTextView.setText("Цена $: "+carPrice);
    }
}
