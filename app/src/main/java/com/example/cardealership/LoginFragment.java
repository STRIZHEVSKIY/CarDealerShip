package com.example.cardealership;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cardealership.databinding.ActivityLoginFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ActivityLoginFragmentBinding binding;
    private OnLoginInteractionListener mListener;

    public interface OnLoginInteractionListener {
        void onLoginSuccess();
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginInteractionListener) {
            mListener = (OnLoginInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        binding = ActivityLoginFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailEditText1.getText().toString().trim();
                String password = binding.passwordEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    String userId = user.getUid();
                                    mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                String role = snapshot.child("role").getValue(String.class);
                                                saveUserRole(role); // Сохранить роль пользователя в SharedPreferences
                                                redirectToAppropriateActivity(role);
                                                if (mListener != null) {
                                                    mListener.onLoginSuccess();
                                                }
                                            } else {
                                                Toast.makeText(getContext(), "Не удалось получить роль пользователя", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(getContext(), "Ошибка базы данных", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Находим кнопки и текстовое поле
        Button registerButton = view.findViewById(R.id.registerButton);
        TextView forgotPasswordTextView = view.findViewById(R.id.forgotPasswordTextView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обработчик клика для кнопки регистрации
                // Переход на экран регистрации
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new RegisterFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });




        return view;
    }

    // Метод для сохранения роли пользователя в SharedPreferences
    private void saveUserRole(String role) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserRole", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("role", role);
        editor.apply();
    }

    // Метод для перенаправления пользователя на соответствующий экран в зависимости от его роли
    private void redirectToAppropriateActivity(String role) {
        if ("admin".equals(role)) {
            // Redirect to Admin Activity
            Intent intent = new Intent(getActivity(), SecondActivity2.class);
            startActivity(intent);
        } else {
            // Redirect to User Activity
            Intent intent = new Intent(getActivity(), SecondActivity.class);
            startActivity(intent);
        }
        getActivity().finish(); // Закрыть текущую активность
    }
}
