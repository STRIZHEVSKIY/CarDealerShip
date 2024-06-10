package com.example.cardealership;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextName;
    private EditText editTextGroup;
    private EditText editTextYear;
    private EditText editTextEmail;
    private ImageView imageViewProfile;
    private Button buttonSave;
    private Button buttonChangeAvatar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private String avatarBase64 = "";

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editTextName = findViewById(R.id.editTextName);
        editTextGroup = findViewById(R.id.editTextGroup);
        editTextYear = findViewById(R.id.editTextYear);
        editTextEmail = findViewById(R.id.editTextEmail);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        buttonSave = findViewById(R.id.buttonSave);
        buttonChangeAvatar = findViewById(R.id.buttonChangeAvatar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mStorage = FirebaseStorage.getInstance().getReference("avatars");

        buttonChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });

        loadUserData();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                imageViewProfile.setImageBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                avatarBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT); // Присваиваем переменной avatarBase64 значение
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            mDatabase.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot.exists()) {
                            UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                            if (userProfile != null) {
                                editTextName.setText(userProfile.getName());
                                editTextGroup.setText(userProfile.getGroup());
                                editTextYear.setText(userProfile.getYear());
                                editTextEmail.setText(userProfile.getEmail());
                            }
                        }
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Не удалось загрузить данные", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void saveUserData() {
        String name = editTextName.getText().toString().trim();
        String group = editTextGroup.getText().toString().trim();
        String year = editTextYear.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(group) && !TextUtils.isEmpty(year) && !TextUtils.isEmpty(email)) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                UserProfile userProfile = new UserProfile(name, group, year, email, avatarBase64);

                // Добавляем роль пользователя в профиль
                String role = getRoleFromSharedPreferences();
                userProfile.setRole(role);

                mDatabase.child(userId).setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, "Данные обновлены", Toast.LENGTH_SHORT).show();
                            uploadImage();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Ошибка сохранения данных", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }

    // Метод для получения роли пользователя из SharedPreferences
    private String getRoleFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserRole", Context.MODE_PRIVATE);
        return sharedPreferences.getString("role", "");
    }

    private void uploadImage() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorage.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + ".jpg");

            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            mDatabase.child(userId).child("avatar").setValue(imageUrl);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
