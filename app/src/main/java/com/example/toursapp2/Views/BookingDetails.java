package com.example.toursapp2.Views;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.toursapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class BookingDetails extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    FirebaseFirestore firebaseFirestore;
    Button btnsave,btncancel;

    String cust_id,doc_id,book_id,partner_id;
    long wallet_balance;
    StringBuilder UID;



    String partName;
    TextView custname,custtime,custmobile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);
        btnsave = findViewById(R.id.btn_book);
        custname = findViewById(R.id.txt_cust_name);
        custtime = findViewById(R.id.txt_cust_time);
        custmobile = findViewById(R.id.txt_cust_number);
        btnsave = findViewById(R.id.save);
        btncancel = findViewById(R.id.cancel);




        cust_id = FirebaseAuth.getInstance().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        book_id=getIntent().getStringExtra("booking_id");
        partName = getIntent().getStringExtra("partner_name");
        partner_id=getIntent().getStringExtra("partner_id");


        firebaseFirestore.collection("USERS")
                .whereEqualTo("cust_id", cust_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {


                                doc_id = documentSnapshot.getId();
                                wallet_balance = (long) documentSnapshot.get("cust_wallet");
                            }


                        } else {

                        }

                    }
                });

        custtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingDetails.this.finish();

            }
        });


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Map<String, Object> data = new HashMap<>();

                data.put("booking_id", book_id);
                data.put("cust_name", custname.getText().toString());
                data.put("cust_number", custmobile.getText().toString());
                data.put("cust_time", custtime.getText().toString());
                data.put("cust_id", cust_id);
                data.put("partner_id", partner_id);
                data.put("partner_name",partName);

                firebaseFirestore.collection("Bookings")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {


                                DocumentReference washingtonRef = firebaseFirestore.collection("USERS").document(doc_id);
                                washingtonRef
                                        .update("cust_wallet", wallet_balance + 200)

                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                BookingDetails.this.finish();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



            }


        });



            }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = (TextView) findViewById(R.id.textView);
        custtime.setText("Hour: " + hourOfDay + " Minute: " + minute);
    }



}
