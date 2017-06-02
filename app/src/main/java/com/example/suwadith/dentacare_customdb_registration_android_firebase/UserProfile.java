package com.example.suwadith.dentacare_customdb_registration_android_firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private StorageReference storage;

    private ImageView userPic;
    private TextView userName;
    private Button changeImageButton;
    private TextView userEmail;
    private TextView userDeviceID;
    private TextView userAddress;
    private Button logoutButton;
    private Uri profileImgURL;
    private static String emailID;
    private ProgressDialog progressDialog;
    private static String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        storage = FirebaseStorage.getInstance().getReference();
        //Initialization of the FirebaseAuth Object
        firebaseAuth = FirebaseAuth.getInstance().getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);

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
        emailID = user.getEmail();

        /*try{
            userPic.setImageIcon(storage.child("Photos").child(emailID).child("myImage.jpg"));
        }catch(){

        }*/

        storage.child("Photos").child(emailID).child("myImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.with(UserProfile.this).load(uri).into(userPic);
                Log.d("Output", uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("Output", "Image Not Found");
            }
        });

        final Query userQuery = users.orderByChild("Email");

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot post : dataSnapshot.getChildren() ){
                    if(post.child("Email").getValue().equals(user.getEmail())){
                        Log.d("Output", "Found");
                        Log.d("Output", post.getKey().toString());
                        userName.setText(post.getKey().toString());
                        name=post.getKey().toString();
                        userDeviceID.setText(post.child("DeviceID").getValue().toString());
                        userAddress.setText(post.child("Address").getValue().toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK){

            progressDialog.setMessage("Uploading ..... ");
            progressDialog.show();

            profileImgURL = data.getData();

            /*StorageReference imagePath = storage.child("Photos").child(emailID).child(profileImgURL.getLastPathSegment());*/

            StorageReference imagePath = storage.child("Photos").child(emailID).child("myImage.jpg");

            DatabaseReference users = database.getReference("users");
            users.child(name).child("ImagePath").setValue(imagePath.toString());

            Log.d("Output", imagePath.toString());

            imagePath.putFile(profileImgURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(UserProfile.this, "Upload Finished", Toast.LENGTH_LONG).show();

                    progressDialog.dismiss();

                }
            });

        }
        startActivity(new Intent(this, UserProfile.class));
    }

    private void changeImage(){

        /*FirebaseStorage imageStorage = FirebaseStorage.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference storageRef = imageStorage.getReferenceFromUrl("gs://dentacare-47bea.appspot.com");*/

    }
}