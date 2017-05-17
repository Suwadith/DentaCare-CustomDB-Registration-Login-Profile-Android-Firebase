package com.example.suwadith.dentacare_customdb_registration_android_firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private TextView userEmail;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Initialization of the FirebaseAuth Object
        firebaseAuth = FirebaseAuth.getInstance().getInstance();

        //Checking whether a user as already Logged In
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            //Starting the User Login Activity if the user is not Logged in
            startActivity(new Intent(this, Login.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        //Retrieving EditText field values from the XML and storing them in java Variables
        userEmail = (TextView) findViewById(R.id.userEmail);

        //Setting the userEmail field text to show the logged in user's email ID
        userEmail.setText("Email: " +user.getEmail());

        userEmail = (TextView) findViewById(R.id.userEmail);
        logoutButton = (Button) findViewById(R.id.logoutButton);

        //Adding the listener function to the logout Button
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //when logoutButton is clicked logoutButton method is invoked
        if(view == logoutButton){
            //Signs out the current logged in user
            firebaseAuth.signOut();
            finish();
            //Switches to login Activity
            startActivity(new Intent(this, Login.class));
        }
    }


    private void changeImage(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

    }
}