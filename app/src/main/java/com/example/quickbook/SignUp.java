package com.example.quickbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView textViewToggle;
    Button buttonLogin, buttonSignup;
    EditText editTextName, editTextEmail, editTextSignUpPassword,  editTextSignUpConfirmPassword , editTextSignUpName,
            editTextSignUpPhone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = findViewById(R.id.editTextSignUpEmailAddress);
        editTextSignUpPassword = findViewById(R.id.editTextSignUpPassword);
        editTextSignUpConfirmPassword = findViewById(R.id.editTextSignUpConfirmPassword);
        editTextSignUpName = findViewById(R.id.editTextSignUpName);
        editTextSignUpPhone = findViewById(R.id.editTextSignUpPhone);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void toLogin(View view){
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void doSignup(View view) {

        Log.i("Action", "Sign up");
        Toast.makeText(this, "Creating account...", Toast.LENGTH_LONG).show();
        final String email = editTextEmail.getText().toString();
        String password = editTextSignUpPassword.getText().toString();
        String passwordAgain = editTextSignUpConfirmPassword.getText().toString();
        String name = editTextSignUpName.getText().toString();
        String phone = editTextSignUpPhone.getText().toString();

        Log.i("SIGN IN", email + " " + password + " " + passwordAgain);
        if (email.equals("") || password.equals("") || passwordAgain.equals("")) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();

        } else if (!password.equals(passwordAgain)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();

        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> mMap = new HashMap<>();
                                mMap.put("email", email);
                                mMap.put("Phone Number", phone);
                                mMap.put("collegeName", "SPIT");
                                mMap.put("issuedBooks", new ArrayList<String>());
                                mMap.put("memberID", "");
                                mMap.put("Name", name );
                                mMap.put("photo", "");
                                mMap.put("user_choice", new ArrayList<>(Collections.nCopies(127, 0)));


                                assert user != null;
                                db.collection("Users").document(user.getUid()).set(mMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(SignUp.this, AdminHomePage.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                            } else {
                                Log.i("FAIL", "Sign Up failed " + task.getException());
                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

        }
    }
}