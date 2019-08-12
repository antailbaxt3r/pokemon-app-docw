package com.example.pokemonappdocw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {


    private EditText password, confirmPassword;
    private EditText name, email, contact;
    private LinearLayout registerButton, alreadyRegistered;
    private String nameText, passwordText, emailText, confirmPasswordText, contactText;
    private LinearLayout progressBar;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name_register);
        email = findViewById(R.id.email_register);
        contact = findViewById(R.id.contact_register);
        password = findViewById(R.id.password_register);
        confirmPassword = findViewById(R.id.confirm_password_register);
        registerButton = findViewById(R.id.register_button);
        alreadyRegistered = findViewById(R.id.alreadyRegistered);
        progressBar = findViewById(R.id.progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                nameText = name.getText().toString();
                emailText = email.getText().toString();
                passwordText = password.getText().toString();
                confirmPasswordText = confirmPassword.getText().toString();
                contactText = contact.getText().toString();

                if(!(nameText.isEmpty()) && !(emailText.isEmpty()) && !(contactText.isEmpty()) &&  !(passwordText.isEmpty()) && !(confirmPasswordText.isEmpty())){
                    if(passwordText.equals(confirmPasswordText)){
                        registerUser();

                    }else{
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void registerUser(){

        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if(passwordText.length() < 6){
            password.setError("Password length should be atleast 6 characters long");
            password.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }



        firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            final FirebaseUser user = firebaseAuth.getCurrentUser();

                            System.out.println("USER is : " + user);
                            System.out.println("UID is: " + user.getUid());

                            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    userReference.child("Users").child(user.getUid()).child("username").setValue(nameText);
                                    userReference.child("Users").child(user.getUid()).child("contactNumber").setValue(contactText);
                                    userReference.child("Users").child(user.getUid()).child("UID").setValue(user.getUid());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            finish();
                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(intent);

                        }else{

                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(RegisterActivity.this, "You are already registered", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(RegisterActivity.this, "User could not be registered. Please try again.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                });

    }
}
