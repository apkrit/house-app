/*
 *   @author Anthony Kritikos
 *   House Decoration Project
 *   05/01/2022
 */
package com.EZPZ.housedecorators;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;

public class EnterpaymentActivity extends AppCompatActivity {
    String uID = "";
    String bundleList = "";
    String numUniqueItems = "";
    String street = "";
    String city = "";
    String state = "";
    String zip = "";
    String userSelectedDate = "";
    String userSelectedDay = "";
    String userSelectedTime = "";
    String totalCost = "";
    String[] bundleSerialNos;
    private float totalBundleCost_micro = 0;
    private float itemTotal;
    private HashMap<String, String> bundleHashMap = new HashMap<>();
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreDecorationList;
    private FirestoreRecyclerAdapter adapter;
    private View googlePayButton;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterpayment);

        // Makes view adapt up when keyboard is opened
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            uID = extras.getString("KEY");
            bundleList = extras.getString("bundleList");
            numUniqueItems = extras.getString("bundleNumItems");
            totalCost = extras.getString("bundleTotalMicro");
            street = extras.getString("street");
            city = extras.getString("city");
            state = extras.getString("state");
            zip = extras.getString("zip");
            userSelectedDate = extras.getString("date");
            userSelectedDay = extras.getString("day");
            userSelectedTime = extras.getString("time");
        }

        ConstraintLayout mConstraintLayout =
                (ConstraintLayout)findViewById(R.id.enterpayment_layout);
        mConstraintLayout.setBackgroundColor(Color.rgb(169, 199, 207));

        TextView userdate = (TextView) findViewById(R.id.eventdateTextView);
        String dateDisplayString =
                userSelectedDate + " " + userSelectedDay + " " + userSelectedTime;
        userdate.setText(dateDisplayString);

        TextView totalTextView = (TextView) findViewById(R.id.totalTextView);
        totalTextView.setText(totalCost);

            firebaseFirestore = FirebaseFirestore.getInstance();
            mFirestoreDecorationList = findViewById(R.id.paymentRecyclerView);

            int items = Integer.parseInt(numUniqueItems);
            parseBundle(bundleList, items);

            Query query = firebaseFirestore.collection("decorationInventory")
                    .orderBy("decorationType")
                    .whereIn("itemID", Arrays.asList(bundleSerialNos));
            FirestoreRecyclerOptions<DecorationModel> options = new
                    FirestoreRecyclerOptions.Builder<DecorationModel>()
                    .setQuery(query, DecorationModel.class).build();
            adapter = new FirestoreRecyclerAdapter<DecorationModel, DecorationsViewHolder>(options){
                @NonNull
                @Override
                public DecorationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.activity_decorationreviewitem, parent,
                                    false);
                    return new DecorationsViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull DecorationsViewHolder holder, int position,
                                                @NonNull DecorationModel model) {

                    String numItemsString = bundleHashMap.get(model.getItemID());
                    int numItemsInt = Integer.parseInt(numItemsString);

                    itemTotal = numItemsInt * model.getPrice_micro();
                    itemTotal /= 100;
                    String totalString = String.format("%.2f", itemTotal);
                    holder.decorationCurrNumAndPrice
                            .setText("(x" + numItemsInt + ") $" + totalString);

                    holder.decorationName.setText(model.getDecorationName());
                    holder.decorationDescription.setText(model.getItemDescription());
                    float total_micro = model.getPrice_micro();
                    String tempPriceString;

                    total_micro /= 100;
                    tempPriceString = String.format("%.2f", total_micro);
                    holder.decorationPrice.setText(tempPriceString);
                    Glide.with(mFirestoreDecorationList)
                            .load(model.getImageURL()).into(holder.imageView);
                }
            };

            //View Holder
            mFirestoreDecorationList.setHasFixedSize(true);
            mFirestoreDecorationList.setLayoutManager(new LinearLayoutManager(this));
            mFirestoreDecorationList.setAdapter(adapter);

            // Payment Transaction would be implemented/called here

            // Save install information to event hashmap and then save to database if logged in as
            // user. Could also save to another database, for company use
            HashMap<String, Object> installEvent = new HashMap<>();
            installEvent.put("date", userSelectedDate);
            installEvent.put("time", userSelectedTime);
            installEvent.put("bundle", bundleList);
            installEvent.put("total", totalCost);
            installEvent.put("type", "installation");
            if(uID != null){
                firebaseFirestore.collection("userData").document(uID)
                        .collection("scheduledEvents").add(installEvent);
            }

    }

    public void openPaymentWindow(View view) {
        Intent i = new Intent(this, PaymentsuccessActivity.class);
        i.putExtra("KEY", uID);
        startActivity(i);
    }

    private void parseBundle(String bundleList, int numUniqueItems) {
        String[] bundleItems = bundleList.split(",", numUniqueItems);
        bundleHashMap = new HashMap<>();
        bundleSerialNos = new String[numUniqueItems];

        int index = 0;
        for(index = 0; index < numUniqueItems; index++){

            String itemListing = bundleItems[index];
            String[] itemListingParts = itemListing.split(" ", 2);

            String serialNo = itemListingParts[0];
            String amount = itemListingParts[1];

            if(amount.charAt(amount.length() - 1) == ','){
                amount = amount.substring(0, (amount.length() - 1)); // Strips comma
            }

            bundleSerialNos[index] = serialNo;
            bundleHashMap.put(serialNo, amount);
        }

    }

    private class DecorationsViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView decorationName;
            private TextView decorationDescription;
            private TextView decorationPrice;
            private TextView decorationCurrNumAndPrice;
            private Button plusButton;
            private Button minusButton;

            public DecorationsViewHolder(@NonNull View itemView){
                super(itemView);
                decorationName = itemView.findViewById(R.id.decorationName);
                decorationDescription = itemView.findViewById(R.id.itemDescription);
                decorationPrice = itemView.findViewById(R.id.decorationPrice);
                decorationCurrNumAndPrice = itemView.findViewById(R.id.decorationCurrNumAndPrice);
                imageView = itemView.findViewById(R.id.imageView);
                plusButton = (Button) itemView.findViewById(R.id.plusButton);
                minusButton = (Button) itemView.findViewById(R.id.minusButton);
            }

        }

        @Override
        protected void onStart() {
            super.onStart();
            adapter.startListening();
        }

        @Override
        protected void onStop() {
            super.onStop();
            adapter.stopListening();
        }

}

