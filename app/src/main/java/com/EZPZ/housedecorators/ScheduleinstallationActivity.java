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

import java.util.HashMap;
import java.util.Map;

public class ScheduleinstallationActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    RecyclerView mFirestoreScheduleList;
    private FirestoreRecyclerAdapter adapter;
    CalendarView schedCalendar;
    TextView dateTextView;
    String uID = "";
    String bundleList = "";
    String numUniqueItems = "";
    String street = "";
    String city = "";
    String state = "";
    String zip = "";
    String installDate = "1/1/1977";
    String userSelectedDate = "";
    String userSelectedDay = "";
    String userSelectedTime = "";
    String totalBundleCostMicroString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduleinstallation);

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

        ConstraintLayout mConstraintLayout = (ConstraintLayout)findViewById(R.id.schedule_layout);
        mConstraintLayout.setBackgroundColor(Color.rgb(169, 199, 207));

        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreScheduleList = findViewById(R.id.schedule_list);

        schedCalendar = (CalendarView) findViewById(R.id.calendarView);
        dateTextView = (TextView) findViewById(R.id.userDateSelectionTextView);

        schedCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1,
                                            int i2) {
                int yearInt = i;
                int monthInt = i1 + 1; // Add 1 because Jan is 0
                int dayInt = i2;

                installDate = monthInt + "/" + dayInt + "/" + yearInt;

                Query newQuery = firebaseFirestore.collection("availableEvents")
                        .whereEqualTo("zip", zip).whereEqualTo("date", installDate)
                        .orderBy("miltime", Query.Direction.ASCENDING);;
                FirestoreRecyclerOptions<ScheduleModel> newOptions = new FirestoreRecyclerOptions
                        .Builder<ScheduleModel>().setQuery(newQuery, ScheduleModel.class).build();
                adapter.updateOptions(newOptions);

                adapter.notifyDataSetChanged();
            }
        });


        Query query = firebaseFirestore.collection("availableEvents")
                .whereEqualTo("zip", zip)
                .whereEqualTo("date", installDate)
                .orderBy("miltime", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<ScheduleModel> options = new
                FirestoreRecyclerOptions.Builder<ScheduleModel>().setQuery(query,
                ScheduleModel.class).build();
        adapter = new FirestoreRecyclerAdapter<ScheduleModel, ScheduleViewHolder>(options) {
            private int row_index;
            private boolean first = true;

            @NonNull
            @Override
            public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_scheduleitem, parent, false);
                return new ScheduleViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position,
                                            @NonNull ScheduleModel model) {
                holder.date.setText(model.getDate());
                holder.year.setText(model.getYear());
                holder.month.setText(model.getMonth());
                holder.day.setText(model.getDay());
                holder.dayofweek.setText(model.getDayofweek());
                holder.time.setText(model.getTime());
                int itemPosition = holder.getAbsoluteAdapterPosition();
                holder.dateCheckButton.setChecked(false);

                holder.dateCheckButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        row_index = itemPosition;
                        notifyDataSetChanged();
                    }

                });

                if((row_index == itemPosition) && (!first)){
                    holder.dateCheckButton.setChecked(true);

                    userSelectedDate = model.getDate();
                    userSelectedDay = model.getDayofweek();
                    userSelectedTime = model.getTime();

                    String schedule = "You selected: " + userSelectedDate + " " + userSelectedDay +
                            " " + userSelectedTime;

                    dateTextView.setText(schedule);

                } else{
                    holder.dateCheckButton.setChecked(false);
                    first = false;
                }

            }

        };

        //View Holder
        mFirestoreScheduleList.setHasFixedSize(true);
        mFirestoreScheduleList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreScheduleList.setAdapter(adapter);

    }

    public void enterPaymentInfo(View view) {
        Intent i = new Intent(this, EnterpaymentActivity.class);
        i.putExtra("KEY", uID);
        i.putExtra("bundleList", bundleList);
        i.putExtra("bundleNumItems", numUniqueItems);
        i.putExtra("bundleTotalMicro", totalBundleCostMicroString);
        i.putExtra("street", street);
        i.putExtra("city", city);
        i.putExtra("state", state);
        i.putExtra("zip", zip);
        i.putExtra("date", userSelectedDate);
        i.putExtra("day", userSelectedDay);
        i.putExtra("time", userSelectedTime);
        startActivity(i);
    }

    private class ScheduleViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView year;
        private TextView month;
        private TextView day;
        private TextView dayofweek;
        private TextView time;
        private CheckBox dateCheckButton;

        public ScheduleViewHolder(@NonNull View itemView){
            super(itemView);
            date = itemView.findViewById(R.id.dateTextView);
            year = itemView.findViewById(R.id.yearTextView);
            month = itemView.findViewById(R.id.monthTextView);
            day = itemView.findViewById(R.id.dayTextView);
            dayofweek = itemView.findViewById(R.id.dayofweekTextView);
            time = itemView.findViewById(R.id.timeTextView);
            dateCheckButton = (CheckBox) itemView.findViewById(R.id.dateCheckBox);
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
