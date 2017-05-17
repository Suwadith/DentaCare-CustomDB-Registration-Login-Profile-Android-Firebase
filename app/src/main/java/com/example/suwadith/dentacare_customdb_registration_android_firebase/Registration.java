package com.example.suwadith.dentacare_customdb_registration_android_firebase;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class Registration extends AppCompatActivity implements View.OnClickListener{

    private EditText editUsername;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editDeviceID;
    private EditText editAddress;

    private Button registerButton;
    private TextView loginRedirect;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Initialization of the FirebaseAuth Object
        firebaseAuth = FirebaseAuth.getInstance();

        //Checking whether a user as already Logged In
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            //Starting the User Profile Activity if the user is already Logged in
            startActivity(new Intent(getApplicationContext(), UserProfile.class));
        }

        //Initialization of the ProgressDialog object
        progressDialog = new ProgressDialog(this);

        //Retrieving EditText field values from the XML and storing them in java Variables
        editUsername = (EditText) findViewById(R.id.editUsername);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editDeviceID = (EditText) findViewById(R.id.editDeviceID);
        editAddress = (EditText) findViewById(R.id.editAddress);

        registerButton = (Button) findViewById(R.id.registerButton);

        loginRedirect = (TextView) findViewById(R.id.loginRedirect);

        //Adding the listener function to both the register button and the login redirection link (Text)
        registerButton.setOnClickListener(this);
        loginRedirect.setOnClickListener(this);

    }

    private void registerNewUser() {

        //Converting EditText type variables to String type variables
        final String username = editUsername.getText().toString().trim();
        final String email = editEmail.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();
        final String deviceID = editDeviceID.getText().toString().trim();
        final String address = editAddress.getText().toString().trim();


        //Checking whether the email field is empty and displaying a error message through Toast
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        //Checking whether the password field is empty and displaying a error message through Toast
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a preferred password", Toast.LENGTH_SHORT).show();
            return;
        }


        /**Have to implement the DeviceID cross check**/


        //Giving the ProgressDialog a message to display while the action in is progress
        progressDialog.setMessage("In Progress...");
        //Displaying the ProgressDialog
        progressDialog.show();

        /**
         *
         * @param email user email
         * @param password user password
         */
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                //checks whether the user has been successfully registered
                if(task.isSuccessful()){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference users = database.getReference("users");
                    users.child(username).child("Email").setValue(email);
                    users.child(username).child("Password").setValue(password);
                    users.child(username).child("DeviceID").setValue(deviceID);
                    users.child(username).child("Address").setValue(address);
                    /*DatabaseReference myRef = database.getReference("message");
                    myRef.setValue("World");*/
                    //Displays a registration successful message through Toast
                    Toast.makeText(Registration.this, "User Registered", Toast.LENGTH_SHORT).show();
                    finish();
                    //Redirects to the User Profile Activity
                    startActivity(new Intent(getApplicationContext(), UserProfile.class));
                }else{
                    //Displays a registration Unsuccessful message through Toast
                    Toast.makeText(Registration.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Method to switch from Registration page to the Login Page
    private void redirectToLogin(){
        finish();
        //starts the Login Activity
        startActivity(new Intent(this, Login.class));
    }

    @Override
    public void onClick(View view) {
        //when registerButton is clicked registerNewUser method is invoked
        if(view == registerButton){
            registerNewUser();
        }

        //when loginRedirect button is clicked redirectToLogin method is invoked
        if(view == loginRedirect){
            redirectToLogin();
        }

    }
}
