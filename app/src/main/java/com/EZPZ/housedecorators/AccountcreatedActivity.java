/*
 *   @author Anthony Kritikos
 *   House Decoration Project
 *   05/01/2022
 */
package com.EZPZ.housedecorators;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class AccountcreatedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountcreated);

        ConstraintLayout mConstraintLayout =
                (ConstraintLayout)findViewById(R.id.accountcreated_layout);
        mConstraintLayout.setBackgroundColor(Color.rgb(177, 237, 254));

    }

    public void returnToLoginView(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}