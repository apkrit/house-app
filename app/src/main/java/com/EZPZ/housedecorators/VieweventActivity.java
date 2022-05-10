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
import android.widget.CalendarView;
import android.widget.CheckBox;
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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class VieweventActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    RecyclerView mFirestoreScheduleList;
    private FirestoreRecyclerAdapter adapter;
    String uID = "";
    String bundleList = "";
    String numUniqueItems = "";
    String street = "";
    String city = "";
    String state = "";
    String zip = "";
    String total = "";
    String type = "";
    String installDate = "1/1/1977";
    String userSelectedDate = "";
    String userSelectedDay = "";
    String userSelectedTime = "";
    String totalBundleCostMicroString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewevent);

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

        ConstraintLayout mConstraintLayout = (ConstraintLayout)findViewById(R.id.viewevent_layout);
        mConstraintLayout.setBackgroundColor(Color.rgb(169, 199, 207));

        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreScheduleList = findViewById(R.id.bundlestringListRecyclerView);

        Query query = firebaseFirestore.collection("userData").document(uID)
                .collection("scheduledEvents").orderBy("date");
        FirestoreRecyclerOptions<UsereventsModel> options = new FirestoreRecyclerOptions
                .Builder<UsereventsModel>().setQuery(query, UsereventsModel.class).build();
        adapter = new FirestoreRecyclerAdapter<UsereventsModel, UsereventsViewHolder>(options) {

            @NonNull
            @Override
            public UsereventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_usereventsitem, parent, false);
                return new UsereventsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsereventsViewHolder holder, int position,
                                            @NonNull UsereventsModel model) {
                holder.date.setText(model.getDate());
                holder.total.setText(model.getTotal());
                holder.type.setText(model.getType());
                holder.time.setText(model.getTime());
                holder.bundle.setText(model.getBundle());
                int itemPosition = holder.getAbsoluteAdapterPosition();
            }

        };

        //View Holder
        mFirestoreScheduleList.setHasFixedSize(true);
        mFirestoreScheduleList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreScheduleList.setAdapter(adapter);

    }

    private class UsereventsViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView total;
        private TextView type;
        private TextView time;
        private TextView bundle;

        public UsereventsViewHolder(@NonNull View itemView){
            super(itemView);
            bundle = itemView.findViewById(R.id.userbundleTextView);
            date = itemView.findViewById(R.id.userdateTextView);
            time = itemView.findViewById(R.id.usertimeTextView);
            total = itemView.findViewById(R.id.usertotalTextView);
            type = itemView.findViewById(R.id.usereventtypetextView);
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
