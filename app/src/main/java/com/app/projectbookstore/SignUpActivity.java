package com.app.projectbookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etStudentID, etNewPassword;
    private Button btnSignUp;
    private TextView txtBackToLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = findViewById(R.id.btnSignUp);
        txtBackToLogin = findViewById(R.id.txtBackToLogin);
        txtBackToLogin.setPaintFlags(txtBackToLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etStudentID = findViewById(R.id.etStudentID);
        etNewPassword = findViewById(R.id.etNewPassword);
        progressDialog = new ProgressDialog(this);


        txtBackToLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkRegistryInfo();
            }
        });
    }

    private void checkRegistryInfo()
    {
        String userFullName = etName.getText().toString().trim();
        String userEmail = etEmail.getText().toString().trim();
        String userID = etStudentID.getText().toString().trim();
        String userPassword = etNewPassword.getText().toString().trim();

        if(userFullName.isEmpty())
        {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        if(userEmail.isEmpty())
        {
            etEmail.setError("Username is required");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
        {
            etEmail.setError("Email invalid");
            etEmail.requestFocus();
            return;
        }

        if(userID.isEmpty())
        {
            etStudentID.setError("Username is required");
            etStudentID.requestFocus();
            return;
        }

        if(userPassword.isEmpty())
        {
            etNewPassword.setError("Password is required");
            etNewPassword.requestFocus();
            return;
        }

        if(userPassword.length() < 6)
        {
            etNewPassword.setError("Password too short");
            etNewPassword.requestFocus();
            return;
        }

        if(userID.length() < 8)
        {
            etStudentID.setError("Student ID too short");
            etStudentID.requestFocus();
            return;
        }

        progressDialog.setTitle("Logging in");
        progressDialog.setMessage("Checking credentials");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        registerUser(userID, userFullName, userEmail, userPassword);
    }

    private void registerUser(String userID, String userFullName, String userEmail, String userPassword)
    {
        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(userID).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("userID", userID);
                    userdataMap.put("userFullName", userFullName);
                    userdataMap.put("userEmail", userEmail);
                    userdataMap.put("userPassword", userPassword);
                    userdataMap.put("userPoints", 0);
                    databaseReference.child("Users").child(userID).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(SignUpActivity.this, "Account Registered", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(SignUpActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
                else {
                    Toast.makeText(SignUpActivity.this, userID + " already exists.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}