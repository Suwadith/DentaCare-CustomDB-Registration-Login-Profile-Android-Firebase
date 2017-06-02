package com.example.suwadith.dentacare_customdb_registration_android_firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private ImageView userPic;
    private TextView userName;
    private Button changeImageButton;
    private TextView userEmail;
    private TextView userDeviceID;
    private TextView userAddress;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Initialization of the FirebaseAuth Object
        firebaseAuth = FirebaseAuth.getInstance().getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("users");

        //Checking whether a user as already Logged In
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            //Starting the User Login Activity if the user is not Logged in
            startActivity(new Intent(this, Login.class));
        }

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        userPic = (ImageView) findViewById(R.id.userPic);
        userName = (TextView) findViewById(R.id.username);
        changeImageButton = (Button) findViewById(R.id.changeImageButton);
        userDeviceID = (TextView) findViewById(R.id.userDeviceID);
        userAddress = (TextView) findViewById(R.id.userAddress);



        //Retrieving EditText field values from the XML and storing them in java Variables
        userEmail = (TextView) findViewById(R.id.userEmail);

        //Setting the userEmail field text to show the logged in user's email ID
        userEmail.setText(user.getEmail());

        final String email = user.getEmail();

        final Query userQuery = users.orderByChild("Email");

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot post : dataSnapshot.getChildren() ){
                    if(post.child("Email").getValue().equals(email)){
                        Log.d("Output", "Found");
                        final String username = post.getKey().toString();
                        Log.d("Output", username);
                        userName.setText(username);
                        final String deviceID = post.child("DeviceID").getValue().toString();
                        userDeviceID.setText(deviceID);
                        final String address = post.child("Address").getValue().toString();
                        userAddress.setText(address);
                    }else{
                       Log.d("Output", "Failure");
                    }

                    Log.d("Output", post.child("Email").toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        logoutButton = (Button) findViewById(R.id.logoutButton);

        //Adding the listener function to the logout Button
        logoutButton.setOnClickListener(this);

        changeImageButton.setOnClickListener(this);
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
        }else if(view == changeImageButton){
                // file picker
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), 100);

        }
    }


    private void changeImage(){

        FirebaseStorage imageStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = imageStorage.getReferenceFromUrl("gs://dentacare-47bea.appspot.com");


    }
}