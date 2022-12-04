package com.app.projectbookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectbookstore.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import io.paperdb.Paper;

public class AdminProfileActivity extends AppCompatActivity {

    private ImageView imgAdmin;
    private EditText adminName, adminEmail;
    private TextView btnChangeProfile, btnClose, btnSave;
    private Button btnAdminLogout;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        btnAdminLogout = (Button) findViewById(R.id.admin_btnLogout);
        imgAdmin = (ImageView) findViewById(R.id.admin_settings_profile_image);
        adminName = (EditText) findViewById(R.id.admin_settings_full_name);
        adminEmail = (EditText) findViewById(R.id.admin_settings_email);
        btnChangeProfile = (TextView) findViewById(R.id.admin_profile_image_change_btn);
        btnClose = (TextView) findViewById(R.id.admin_close_settings_btn);
        btnSave = (TextView) findViewById(R.id.admin_update_account_settings_btn);


        userInfoDisplay(imgAdmin, adminName, adminEmail);

        btnAdminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(AdminProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(intent);
                finish();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

        btnChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                openFileChooser();
            }
        });
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("userFullName", adminName.getText().toString());
        userMap. put("userEmail", adminEmail.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getUserID()).updateChildren(userMap);
        startActivity(new Intent(AdminProfileActivity.this, AdminHomeScreenActivity.class));
        Toast.makeText(AdminProfileActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST  &&  resultCode == RESULT_OK  &&  data!=null)
        {
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(imgAdmin);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(AdminProfileActivity.this, AdminProfileActivity.class));
            finish();
        }
    }




    private void userInfoSaved()
    {
        if (TextUtils.isEmpty(adminName.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(adminEmail.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePictureRef
                    .child(Prevalent.currentOnlineUser.getUserID() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap. put("userFullName", adminName.getText().toString());
                        userMap. put("userEmail", adminEmail.getText().toString());
                        userMap. put("userImage", myUrl);
                        ref.child(Prevalent.currentOnlineUser.getUserID()).updateChildren(userMap);
                        progressDialog.dismiss();
                        startActivity(new Intent(AdminProfileActivity.this, AdminHomeScreenActivity.class));
                        Toast.makeText(AdminProfileActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(AdminProfileActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final ImageView profileImageView, final EditText etName,  final EditText etEmail)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getUserID());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("userImage").exists())
                    {
                        String image = dataSnapshot.child("userImage").getValue().toString();
                        Picasso.get().load(image).into(profileImageView);
                    }

                    String name = dataSnapshot.child("userFullName").getValue().toString();
                    String email = dataSnapshot.child("userEmail").getValue().toString();

                    etName.setText(name);
                    etEmail.setText(email);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}