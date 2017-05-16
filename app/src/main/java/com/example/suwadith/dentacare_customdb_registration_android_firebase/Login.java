package com.example.suwadith.dentacare_customdb_registration_android_firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText editEmail;
    private EditText editPassword;
    private Button loginButton;
    private TextView registerRedirect;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);

        loginButton = (Button) findViewById(R.id.loginButton);

        registerRedirect = (TextView) findViewById(R.id.registerRedirect);

        //Adding the listener function to both the login button and the register redirection link (Text)
        loginButton.setOnClickListener(this);
        registerRedirect.setOnClickListener(this);

    }

    private void loginUser() {

        //Converting EditText type variables to String type variables
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

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

        //Giving the ProgressDialog a message to display while the action in is progress
        progressDialog.setMessage("In Progress...");
        //Displaying the ProgressDialog
        progressDialog.show();

        /**
         *
         * @param email user email
         * @param password user password
         */
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                //checks whether the user has been successfully logged in
                if(task.isSuccessful()){
                    //Displays a login successful message through Toast
                    Toast.makeText(Login.this, "User Logged In", Toast.LENGTH_SHORT).show();
                    finish();
                    //Redirects to the User Profile Activity
                    startActivity(new Intent(getApplicationContext(), UserProfile.class));
                }else{
                    //Displays a login Unsuccessful message through Toast
                    Toast.makeText(Login.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    //Method to switch from Login page to the Registration Page
    private void redirectToRegister() {
        finish();
        //starts the Registration Activity
        startActivity(new Intent(this, Registration.class));
    }

    @Override
    public void onClick(View view) {

        //when loginButton is clicked loginUser method is invoked
        if(view == loginButton){
            loginUser();
        }

        //when registerRedirect is clicked redirectToRegister method is invoked
        if(view == registerRedirect){
            redirectToRegister();
        }

    }

}