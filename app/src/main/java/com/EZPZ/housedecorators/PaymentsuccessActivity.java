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

public class PaymentsuccessActivity extends AppCompatActivity {
    String uID = "";
    String bundleList = "";
    String numUniqueItems = "";
    String street = "";
    String city = "";
    String state = "";
    String zip = "";
    String totalBundleCostMicroString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentsuccess);

        ConstraintLayout mConstraintLayout =
                (ConstraintLayout)findViewById(R.id.paymentsuccess_layout);
        mConstraintLayout.setBackgroundColor(Color.rgb(177, 237, 254));

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            uID = extras.getString("KEY");
            bundleList = extras.getString("bundleList");
            numUniqueItems = extras.getString("bundleNumItems");
            totalBundleCostMicroString = extras.getString("bundleTotalMicro");
            street = extras.getString("street");
            city = extras.getString("city");
            state = extras.getString("state");
            zip = extras.getString("zip");
        }

    }

    public void returnToHome(View view) {

        Intent i;
        if((uID == null) || (uID.compareTo("") == 0)){
            i = new Intent(this, MainActivity.class); // Back to main menu
        }else{
            i = new Intent(this, HomeownermenuActivity.class); // Back to home menu
            i.putExtra("KEY", uID);
        }
        startActivity(i);

    }

}