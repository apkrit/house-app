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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class DecorationbundleActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreDecorationList;
    private HashMap<String, Integer> decorationBundle = new HashMap<>();
    private float totalBundleCost_micro = 0;
    private int bundleNumItems;
    private float itemTotal;
    private int numItems = 0;
    TextView bundleTextView;
    private FirestoreRecyclerAdapter adapter;
    private String uID;
    private StringBuilder strBuilder = new StringBuilder();
    private String totalBundleString;
    private String bundleNumItemsString;
    private String zipCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decorationbundle);

        RelativeLayout mRelativeLayout = (RelativeLayout)findViewById(R.id.firestore_decorations);
        mRelativeLayout.setBackgroundColor(Color.rgb(169, 199, 207));

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            uID = extras.getString("KEY");
            zipCode = extras.getString("zip");
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreDecorationList = findViewById(R.id.decorations_list);

        Query query = firebaseFirestore.collection("decorationInventory")
                .orderBy("decorationType").whereEqualTo("zip", zipCode);
        FirestoreRecyclerOptions<DecorationModel> options = new FirestoreRecyclerOptions
                .Builder<DecorationModel>().setQuery(query, DecorationModel.class).build();
        adapter = new FirestoreRecyclerAdapter<DecorationModel, DecorationsViewHolder>(options) {
            @NonNull
            @Override
            public DecorationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .activity_decorationitem, parent, false);
                return new DecorationsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DecorationsViewHolder holder, int position,
                                            @NonNull DecorationModel model) {

                holder.decorationName.setText(model.getDecorationName());
                holder.decorationDescription.setText(model.getItemDescription());
                String tempPriceString;
                float total_micro = model.getPrice_micro();
                total_micro /= 100;
                tempPriceString = String.format("%.2f", total_micro);
                holder.decorationPrice.setText(tempPriceString);
                Glide.with(mFirestoreDecorationList)
                        .load(model.getImageURL()).into(holder.imageView);

                if(!decorationBundle.containsKey(model.getItemID())){
                    decorationBundle.put(model.getItemID(), 0); // Adds decoration to the hashmap
                }else{

                }

                //int itemQuantity = model.getQuantity();
                itemTotal = model.getQuantity() * model.getPrice_micro();
                itemTotal /= 100;
                String totalString = String.format("%.2f", itemTotal);
                holder.decorationCurrNumAndPrice
                        .setText("(x" + model.getQuantity() + ") $" + totalString);

                holder.plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        model.setQuantity(model.getQuantity() + 1);
                        adapter.notifyItemChanged(holder.getAbsoluteAdapterPosition());

                        //noinspection ConstantConditions
                        numItems = decorationBundle.get(model.getItemID()) + 1;
                        decorationBundle.put(model.getItemID(), (numItems));
                        itemTotal = numItems * model.getPrice_micro();
                        itemTotal /= 100;
                        String totalString = String.format("%.2f", itemTotal);
                        holder.decorationCurrNumAndPrice
                                .setText("(x" + numItems + ") $" + totalString);

                        // Formats the cost into proper dollar and cent form
                        totalBundleCost_micro += model.getPrice_micro();
                        totalBundleCost_micro /= 100;
                        totalBundleString = String.format("%.2f", totalBundleCost_micro);
                        bundleTextView = findViewById(R.id.bundleCostTextView);
                        bundleTextView.setText("$" + totalBundleString);
                        totalBundleCost_micro *= 100;

                    }
                });

                holder.minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(model.getQuantity() > 0){

                            model.setQuantity(model.getQuantity() - 1);
                            numItems = decorationBundle.get(model.getItemID()) + 1;
                            decorationBundle.put(model.getItemID(), (model.getQuantity() + 1));
                            adapter.notifyItemChanged(holder.getAbsoluteAdapterPosition());

                            // Formats the cost into proper dollar and cent form
                            totalBundleCost_micro -= model.getPrice_micro();
                            totalBundleCost_micro /= 100;
                            totalBundleString = String.format("%.2f", totalBundleCost_micro);
                            bundleTextView = findViewById(R.id.bundleCostTextView);
                            bundleTextView.setText("$" + totalBundleString);
                            totalBundleCost_micro *= 100;

                        }

                    }

                });

            }

        };

        //View Holder
        mFirestoreDecorationList.setHasFixedSize(true);
        mFirestoreDecorationList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreDecorationList.setAdapter(adapter);

    }

    // Saves the current bundle information before initiating the next intent activity
    public void selectInstallTime(View view) {

        // For all decorations that are more than 0, append the serial no and the amount to a
        // string to be written to user data
        for (Map.Entry<String, Integer> entry : decorationBundle.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            if(value > 0){
                String tempString = (key + " " + value + ",");
                strBuilder.append(tempString);
                bundleNumItems++;
                bundleNumItemsString = String.valueOf(bundleNumItems);
            }

        }

        String bundleListString = String.valueOf(strBuilder);

        Intent i = new Intent(this, ScheduleinstallationActivity.class);
        i.putExtra("KEY", uID);
        i.putExtra("bundleList", bundleListString);
        i.putExtra("bundleNumItems", bundleNumItemsString);
        i.putExtra("bundleTotalMicro", totalBundleString);
        i.putExtra("zip", zipCode);
        startActivity(i);
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
