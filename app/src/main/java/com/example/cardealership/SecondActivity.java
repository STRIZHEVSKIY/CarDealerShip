package com.example.cardealership;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements
        CarListFragment.OnCarListInteractionListener,
        AboutAppFragment.OnAboutAppInteractionListener,
        ProfileFragment.OnProfileInteractionListener,
        UserManualFragment.OnUserManualInteractionListener {

    private CarListFragment carListFragment;
    private AboutAppFragment aboutAppFragment;
    private ProfileFragment profileFragment;
    private UserManualFragment userManualFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Инициализация фрагментов
        carListFragment = new CarListFragment();
        aboutAppFragment = new AboutAppFragment();
        profileFragment = new ProfileFragment();
        userManualFragment = new UserManualFragment();

        // Настройка нижнего меню
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();
                if (id == R.id.nav_car_list) {
                    selectedFragment = carListFragment;
                } else if (id == R.id.nav_about_app) {
                    selectedFragment = aboutAppFragment;
                } else if (id == R.id.nav_user_manual) {
                    selectedFragment = userManualFragment;
                } else if (id == R.id.nav_profile) {
                    selectedFragment = profileFragment;
                }
                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                }
                return true;
            }
        });

        // Показываем список машин по умолчанию
        replaceFragment(carListFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onCarListSelected(List<Car> selectedCars) {

    }

    @Override
    public void onAboutAppInteraction() {
        // Обработка клика на информацию о программе
    }

    @Override
    public void onProfileInteraction() {
        // Обработка клика на профиль
    }

    @Override
    public void onUserManualInteraction() {
        // Обработка клика на инструкцию пользователю
    }
}
