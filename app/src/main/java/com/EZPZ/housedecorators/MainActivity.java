/*
 *   @author Anthony Kritikos
 *   House Decoration Project
 *   05/01/2022
 */
package com.EZPZ.housedecorators;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "APKRIT";
    private static boolean matchFound = false;
    private String zipCode = "";
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout mConstraintLayout = (ConstraintLayout)findViewById(R.id.main_layout);
        mConstraintLayout.setBackgroundColor(Color.rgb(177, 237, 254));

    }

    public void createAccountView(View view) {
        Intent i = new Intent(this, CreateaccountActivity.class);
        startActivity(i);
    }

    public void guestBundleView(View view) {
        EditText userzipcode_edittext = (EditText)findViewById(R.id.userzipTextPostalAddress);
        zipCode = userzipcode_edittext.getText().toString();
        Intent i = new Intent(this, DecorationbundleActivity.class);
        i.putExtra("zip", zipCode);
        startActivity(i);
    }

    public void homeownermenuView(View view) {
        Intent i = new Intent(this, HomeownermenuActivity.class);
        i.putExtra("KEY", auth.getCurrentUser().getUid());
        startActivity(i);
    }

    /** Called when the user touches the button */
    public void loginUser(View view) {
        matchFound = false;
        String username = "";
        String password = "";

        // Get username and password from View fields
        EditText username_edittext = (EditText)findViewById(R.id.editTextTextEmailAddress);
        username = username_edittext.getText().toString();
        EditText password_edittext = (EditText)findViewById(R.id.editTextTextPassword);
        password = password_edittext.getText().toString();

        // Keeps program from crashing when user clicks login with blank form
        if((username.compareTo("") != 0) && (password.compareTo("") != 0)){

            auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(username, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(MainActivity.this, "Login successful.",
                            Toast.LENGTH_SHORT).show();
                    homeownermenuView(view);
                }
            });

            auth.signInWithEmailAndPassword(username, password)
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Login failed.",
                            Toast.LENGTH_SHORT).show();
                }
            });

        } else{
            Toast.makeText(MainActivity.this, "Login failed.",
                    Toast.LENGTH_SHORT).show();
        }

    }

}