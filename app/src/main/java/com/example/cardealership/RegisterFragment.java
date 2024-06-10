// RegisterFragment.java
package com.example.cardealership;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.cardealership.databinding.ActivityRegisterFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ActivityRegisterFragmentBinding binding;

    public interface OnRegisterInteractionListener {
        void onRegister();
    }

    private OnRegisterInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        binding = ActivityRegisterFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailEditText.getText().toString().trim();
                String password = binding.passwordEditText.getText().toString().trim();
                String role = binding.roleEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(role)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            String userId = user.getUid();

                                            // Добавление пользователя с ролью в базу данных
                                            Map<String, String> userProfile = new HashMap<>();
                                            userProfile.put("email", email);
                                            userProfile.put("role", role);

                                            mDatabase.child(userId).setValue(userProfile)
                                                    .addOnCompleteListener(task1 -> {
                                                        if (task1.isSuccessful()) {
                                                            Toast.makeText(getContext(), "Регистрация завершена", Toast.LENGTH_SHORT).show();
                                                            if (mListener != null) {
                                                                mListener.onRegister();
                                                            }
                                                        } else {
                                                            Toast.makeText(getContext(), "Ошибка сохранения данных пользователя. Попробуйте еще раз.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Ошибка регистрации. Попробуйте еще раз.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterInteractionListener) {
            mListener = (OnRegisterInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegisterInteractionListener");
        }
    }
}
