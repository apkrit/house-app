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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CreateaccountActivity extends AppCompatActivity {

    private EditText firstname;
    private EditText lastname;
    private EditText zip;
    private EditText email;
    private EditText password1;
    private EditText password2;
    private Button register;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        ConstraintLayout mConstraintLayout = (ConstraintLayout)findViewById(R.id.createacct_layout);
        mConstraintLayout.setBackgroundColor(Color.rgb(177, 237, 254));

    }

    public void registerNewAccount(View view) {

        firstname = findViewById(R.id.firstName);
        String txtFirstname = firstname.getText().toString();
        lastname = findViewById(R.id.lastName);
        String txtLastname = lastname.getText().toString();
        zip = findViewById(R.id.newUserZip);
        String txtZip = zip.getText().toString();
        email = findViewById(R.id.newUserEmail);
        String txtEmail = email.getText().toString();
        password1 = findViewById(R.id.newUserPassword1);
        String txtPassword1 = password1.getText().toString();
        password2 = findViewById(R.id.newUserPassword2);
        String txtPassword2 = password2.getText().toString();

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(txtPassword1.compareTo(txtPassword2) == 0){

            auth.createUserWithEmailAndPassword(txtEmail, txtPassword1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();

                                String tempUID = auth.getUid();

                                // Save install information to event hashmap and then save to
                                // database if logged in as user. Could also save to another
                                // database, for company use
                                HashMap<String, Object> newUserData = new HashMap<>();
                                newUserData.put("firstname", txtFirstname);
                                newUserData.put("lastname", txtLastname);
                                newUserData.put("zip", txtZip);
                                newUserData.put("email", txtEmail);
                                if(tempUID != null){
                                    firebaseFirestore.collection("userData")
                                            .document(tempUID).set(newUserData);
                                    Toast.makeText(CreateaccountActivity
                                            .this, "Registered account!",
                                            Toast.LENGTH_SHORT).show();
                                    View thisView = findViewById(R.id.createacct_layout);
                                    createAccount(thisView);
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(CreateaccountActivity.this,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }

    public void createAccount(View view) {
        // Go to account creation success page
        Intent i = new Intent(this, AccountcreatedActivity.class);
        startActivity(i);
    }

}