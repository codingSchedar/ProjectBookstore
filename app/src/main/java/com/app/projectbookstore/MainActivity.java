package com.app.projectbookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectbookstore.AdminHomeScreenActivity;
import com.app.projectbookstore.Models.Users;
import com.app.projectbookstore.Prevalent.Prevalent;
import com.app.projectbookstore.R;
import com.app.projectbookstore.StudentHomeScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity
{

    private Button btnLogin;
    private TextView txtSignUp;
    private TextInputEditText etUsername, etPassword;
    private ProgressDialog progressDialog;
    private Character userFirstChar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        txtSignUp = findViewById(R.id.txtSignUp);
        txtSignUp.setPaintFlags(txtSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        progressDialog = new ProgressDialog(this);
        Paper.init(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRegistryInfo();
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    public void toastMsg(String msg)
    {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void checkRegistryInfo()
    {
        String userID = etUsername.getText().toString();
        String userPassword = etPassword.getText().toString();

        if(userID.isEmpty())
        {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }

        if(userPassword.isEmpty())
        {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;

        }

        if(userPassword.length() < 6)
        {
            etPassword.setError("Password is too short");
            etPassword.requestFocus();
            return;

        }

        progressDialog.setTitle("Logging in");
        progressDialog.setMessage("Checking credentials");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        userFirstChar = userID.charAt(0);

        loginUser(userID, userPassword);
    }

    private void loginUser(final String userID, final String userPassword)
    {
        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(userID).exists()){

                    Users usersData = dataSnapshot.child("Users").child(userID).getValue(Users.class);
                    if (usersData.getUserID().equals(userID))
                    {
                        if (usersData.getUserPassword().equals(userPassword))
                        {
                            if(userFirstChar == '0')
                            {
                                Toast.makeText(MainActivity.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent intent = new Intent(MainActivity.this, AdminHomeScreenActivity.class);
                                Prevalent.currentOnlineUser = usersData; // stores who's the user
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Welcome Student", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent intent = new Intent(MainActivity.this, StudentHomeScreenActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }

                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"Incorrect password",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Account doesn't exist'", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}