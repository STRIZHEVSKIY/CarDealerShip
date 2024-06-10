package com.example.cardealership;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnLoginInteractionListener,
        RegisterFragment.OnRegisterInteractionListener{
    private static final String TAG = "myLogs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Показываем фрагмент для входа при запуске приложения
        showLoginFragment();
        Log.d(TAG, "Нажата кнопка 1");
    }



    // Метод для замены текущего фрагмента на фрагмент для входа
    private void showLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
        Log.d(TAG, "Нажата кнопка 2");
    }

    // Обработчик события успешного входа
    @Override
    public void onLoginSuccess() {
        // Получаем роль пользователя из SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserRole", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");

        // Проверяем роль пользователя и переходим на соответствующую активность
        if ("admin".equals(role)) {
            // Redirect to Admin Activity
            Intent intent = new Intent(MainActivity.this, SecondActivity2.class);
            startActivity(intent);
        } else {
            // Redirect to User Activity
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        }
        finish(); // Закрыть текущую активность
        Log.d(TAG, "Нажата кнопка 3");
    }



    // Обработчик события регистрации
    @Override
    public void onRegister() {
        // Показываем фрагмент для регистрации
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RegisterFragment())
                .addToBackStack(null)
                .commit();
        Log.d(TAG, "Нажата кнопка 4");
    }


}
