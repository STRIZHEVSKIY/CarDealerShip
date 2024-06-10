package com.example.cardealership;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FirebaseCarHelper {
    private DatabaseReference databaseReference;

    public FirebaseCarHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference("cars");
    }

    public void addCars(List<Car> cars) {
        for (Car car : cars) {
            String key = databaseReference.push().getKey();
            if (key != null) {
                databaseReference.child(key).setValue(car);
            }
        }
    }

    public interface DataStatus {
        void DataIsLoaded(List<Car> cars, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public void readCars(final DataStatus dataStatus) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Car> cars = new ArrayList<>();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Car car = keyNode.getValue(Car.class);
                    cars.add(car);
                }
                dataStatus.DataIsLoaded(cars, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}
