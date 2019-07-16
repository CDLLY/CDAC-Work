package com.example.cdacserveapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Saving Data ";

    FirebaseFirestore firebaseDatabase = FirebaseFirestore.getInstance();
    Map<String, String> serveData = new HashMap<>();

    public static AppDatabase database;

    private InternetConnectivity connectivity;

    private Button saveBtn, cancelBtn;
    private EditText editTextDescription, editTextUsername;
    private RatingBar ratingBar;
    private TextView ratingBarTextview;

    public ProgressBar progressIndicator;
    public RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElements();

        if (connectivity.isConnected())
            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (ratingBar.getRating() > 0){
                    String rating = String.valueOf(ratingBar.getRating());
                    ratingBarTextview.setVisibility(View.VISIBLE);
                    ratingBarTextview.setText(rating+"/5");
                    saveBtn.setEnabled(true);
                    cancelBtn.setEnabled(true);
                }else {
                    ratingBarTextview.setVisibility(View.GONE);
                    saveBtn.setEnabled(false);
                    cancelBtn.setEnabled(false);
                }
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating() == 0) {
                    Toast.makeText(getApplicationContext(), "Mark Rating", Toast.LENGTH_LONG).show();
                } else if (editTextDescription.getText().length() == 0) {
                    editTextDescription.setError("Please write a few words.");
                } else if (editTextUsername.getText().length() == 0){
                    editTextUsername.setError("Please enter you name.");
                } else {
                    StoreData storeData = new StoreData();
                    storeData.execute();
                }
            }
        });

    }


    private void initElements () {
        saveBtn = findViewById(R.id.saveDataBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBarTextview = findViewById(R.id.ratingBarTextview);
        editTextDescription = findViewById(R.id.edittextDescription);
        progressIndicator = findViewById(R.id.progressIndicator);
        relativeLayout = findViewById(R.id.relativeLayoutServey);
        editTextUsername = findViewById(R.id.edittextUsername);

        connectivity = new InternetConnectivity(this);

        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Servey-Database").build();

    }


    class StoreData extends AsyncTask<Void, Void, Void> {

        ServeyModel model;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            model = new ServeyModel();

            String username = editTextUsername.getText().toString();
            Float rating = ratingBar.getRating();
            String description = editTextDescription.getText().toString();

            model.setUserName(username);
            model.setRating(rating);
            model.setServeyDiscription(description);

            progressIndicator.setVisibility(View.VISIBLE);
            ratingBar.setEnabled(false);
            editTextDescription.setEnabled(false);
            saveBtn.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressIndicator.setVisibility(View.GONE);
            ratingBar.setEnabled(true);
            ratingBar.setRating(0);
            editTextDescription.setEnabled(true);
            editTextDescription.setText("");
            saveBtn.setEnabled(true);
            Toast.makeText(getApplicationContext(), " Data Submitted ", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            database.serveyDao().insert(model);

            firebaseDatabase.collection("serveyData")
                    .add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "onSuccess: data saved");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: "+e.toString());
                }
            });
            return null;
        }
    }

    private static class ReadData extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            List<ServeyModel> data = new ArrayList<>();

            data = database.serveyDao().getAllServeyLsit();

            for (ServeyModel d: data){
                Log.d("\n\tRead Data", "username: "+d.getUserName()+" rating: "+d.getRating());
            }
            return null;
        }
    }

}
