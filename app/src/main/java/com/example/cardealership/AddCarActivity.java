package com.example.cardealership;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AddCarActivity extends AppCompatActivity {
    private EditText editTextModel;
    private EditText editTextYear;
    private EditText editTextPrice;
    private ImageView imageViewCar;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        editTextModel = findViewById(R.id.editTextModel);
        editTextYear = findViewById(R.id.editTextYear);
        editTextPrice = findViewById(R.id.editTextPrice);
        imageViewCar = findViewById(R.id.imageViewCar);

        imageViewCar.setOnClickListener(v -> selectImage());

        findViewById(R.id.buttonSaveCar).setOnClickListener(v -> saveCar());
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageViewCar.setImageURI(imageUri);
        }
    }

    private void saveCar() {
        String model = editTextModel.getText().toString().trim();
        String yearText = editTextYear.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();

        if (model.isEmpty()) {
            Toast.makeText(this, "Введите модель автомобиля", Toast.LENGTH_SHORT).show();
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Введите корректный год выпуска", Toast.LENGTH_SHORT).show();
            return;
        }

        if (year <= 0) {
            Toast.makeText(this, "Год выпуска должен быть больше 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (price.isEmpty()) {
            Toast.makeText(this, "Введите цену автомобиля", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Выберите изображение автомобиля", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadImageToFirebase(model, year, price, imageUri);
    }


    private void uploadImageToFirebase(String model, int year, String price, Uri imageUri) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("car_images/" + UUID.randomUUID().toString());
        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Car newCar = new Car(model, year, price, uri.toString());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("new_car", newCar);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
        ).addOnFailureListener(e -> Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show());
    }
}
