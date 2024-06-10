package com.example.cardealership;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CarListFragment extends Fragment {
    private List<Car> carList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private OnCarListInteractionListener listener;
    private static final int ADD_CAR_REQUEST = 1;
    private static final String PREFS_NAME = "car_prefs";
    private static final String CARS_KEY = "car_list";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCarListInteractionListener) {
            listener = (OnCarListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCarListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadCarsFromPrefs();

        if (carList.isEmpty()) {
            carList.add(new Car("Модель 1", 2022, "10000", "https://firebasestorage.googleapis.com/v0/b/cardealership-1ef24.appspot.com/o/car1.jpg?alt=media&token=c674df23-c769-4b74-a6e9-f17a47ad09e4"));
            carList.add(new Car("Модель 2", 2020, "17400", "https://firebasestorage.googleapis.com/v0/b/cardealership-1ef24.appspot.com/o/car1.jpg?alt=media&token=c674df23-c769-4b74-a6e9-f17a47ad09e4"));
            carList.add(new Car("Модель 3", 2021, "15000", "https://firebasestorage.googleapis.com/v0/b/cardealership-1ef24.appspot.com/o/car1.jpg?alt=media&token=c674df23-c769-4b74-a6e9-f17a47ad09e4"));
            carList.add(new Car("Модель 4", 2018, "18400", "https://firebasestorage.googleapis.com/v0/b/cardealership-1ef24.appspot.com/o/car1.jpg?alt=media&token=c674df23-c769-4b74-a6e9-f17a47ad09e4"));
            carList.add(new Car("Модель 5", 2019, "19000", "https://firebasestorage.googleapis.com/v0/b/cardealership-1ef24.appspot.com/o/car1.jpg?alt=media&token=c674df23-c769-4b74-a6e9-f17a47ad09e4"));
            carList.add(new Car("Модель 6", 2023, "13400", "https://firebasestorage.googleapis.com/v0/b/cardealership-1ef24.appspot.com/o/car1.jpg?alt=media&token=c674df23-c769-4b74-a6e9-f17a47ad09e4"));
            carList.add(new Car("Модель 7", 2022, "12000", "https://firebasestorage.googleapis.com/v0/b/cardealership-1ef24.appspot.com/o/car1.jpg?alt=media&token=c674df23-c769-4b74-a6e9-f17a47ad09e4"));
            carList.add(new Car("Модель 8", 2020, "11400", "https://firebasestorage.googleapis.com/v0/b/cardealership-1ef24.appspot.com/o/car1.jpg?alt=media&token=c674df23-c769-4b74-a6e9-f17a47ad09e4"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_car_list_fragment, container, false);

        if (listener != null) {
            listener.onCarListSelected(carList);
        }

        // Настройка RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewCars);
        carAdapter = new CarAdapter(getContext(), carList);
        recyclerView.setAdapter(carAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CAR_REQUEST && resultCode == Activity.RESULT_OK) {
            Car newCar = (Car) data.getSerializableExtra("new_car");
            carList.add(newCar);
            carAdapter.notifyDataSetChanged();
            saveCarsToPrefs();
        }
    }

    private void saveCarsToPrefs() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(carList);
        editor.putString(CARS_KEY, json);
        editor.apply();
    }

    private void loadCarsFromPrefs() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(CARS_KEY, null);
        Type type = new TypeToken<ArrayList<Car>>() {}.getType();
        carList = gson.fromJson(json, type);
        if (carList == null) {
            carList = new ArrayList<>();
        }
    }

    // Определяем интерфейс для взаимодействия с SecondActivity
    public interface OnCarListInteractionListener {
        void onCarListSelected(List<Car> carList);
    }
}
