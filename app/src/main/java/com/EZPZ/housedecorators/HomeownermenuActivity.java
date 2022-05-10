/*
 *   @author Anthony Kritikos
 *   House Decoration Project
 *   05/01/2022
 */
package com.EZPZ.housedecorators;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class HomeownermenuActivity extends AppCompatActivity implements
        ZipcodeDialog.ZipcodeDialogListener, HomezipDialog.HomezipDialogListener {
    private String uID;
    private String zipCode;
    private String homeZip;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeownermenu);

        ConstraintLayout mConstraintLayout =
                (ConstraintLayout)findViewById(R.id.homeownermenu_layout);
        mConstraintLayout.setBackgroundColor(Color.rgb(177, 237, 254));

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            uID = extras.getString("KEY");
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef =
                firebaseFirestore.collection("userData").document(uID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        zipCode = document.getString("zip");
                        homeZip = zipCode;
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    public void decorationbundleView(View view) {
        Intent i = new Intent(this, DecorationbundleActivity.class);
        i.putExtra("KEY", uID);
        i.putExtra("zip", homeZip);
        startActivity(i);
    }

    public void openNewZipcodeDialog(View view) {
        ZipcodeDialog zipcodeDialog = new ZipcodeDialog();
        zipcodeDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void changeHomeZipcodeDialog(View view) {
        HomezipDialog homezipDialog = new HomezipDialog();
        homezipDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void eventmanagementView(View view) {
        Intent i = new Intent(this, VieweventActivity.class);
        i.putExtra("KEY", uID);
        startActivity(i);
    }

    public void supportView(View view) {
        Intent i = new Intent(this, SupportActivity.class);
        startActivity(i);
    }

    public void xmitZip(String zip){
        zipCode = zip;
        Intent i = new Intent(this, DecorationbundleActivity.class);
        i.putExtra("KEY", uID);
        i.putExtra("zip", zipCode);
        startActivity(i);
    }

    public void homexmitZip(String zip){
        zipCode = zip;
        homeZip = zip;

        // Change the home zipcode and update Firebase database
        firebaseFirestore.collection("userData").document(uID).update("zip", zip);
    }

}