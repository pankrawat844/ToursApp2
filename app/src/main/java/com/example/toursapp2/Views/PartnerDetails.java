package com.example.toursapp2.Views;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.toursapp2.Model.PartnerItemModel;
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

public class PartnerDetails extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    Button btnsubmit,btnpay;
    TextView partName,partAddress,partRating,partCost,partCuisines,partDescription,partFullAddress,partPhoneNo,partTimings,heart,txtfav;
    ImageView partImage;
    String name,user,docid;
    StringBuilder UID;
   long wallet_balance;
    String cust_id,doc_id,book_id,partner_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_details);
        btnsubmit =findViewById(R.id.btn_book);
        heart=findViewById(R.id.txt_heart);
        txtfav=findViewById(R.id.txt_fav);
        partName=findViewById(R.id.txt_partner_name);
        partAddress=findViewById(R.id.txt_part_address);
        partRating=findViewById(R.id.txt_part_rating);
        partTimings=findViewById(R.id.txt_partner_timing);
        partCuisines=findViewById(R.id.txt_partner_cuisines);
        partCost=findViewById(R.id.txt_average_cost);
        partDescription=findViewById(R.id.txt_partner_description);
        partPhoneNo=findViewById(R.id.txt_partner_phoneno);
        partFullAddress=findViewById(R.id.txt_partner_full_address);
        partImage=findViewById(R.id.txt_partner_image);
        btnpay=findViewById(R.id.btn_pay);

        cust_id = FirebaseAuth.getInstance().getUid();
        firebaseFirestore= FirebaseFirestore.getInstance();
        book_id=getUID();
        firebaseFirestore.collection("Favourites")
                .whereEqualTo("user_id",FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                            {

                                String partname= documentSnapshot.get("partner_name").toString();

                                firebaseFirestore.collection("PARTNER LIST")
                                        .whereEqualTo("partnerName",partname)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful())
                                                {
                                                    for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                                                    {

                                                       if(documentSnapshot.get("partner_id").toString().equals(partner_id))
                                                       {
                                                           txtfav.setText("UnFavourite");
                                                       }

                                                    }


                                                }
                                                else
                                                {

                                                    String error=task.getException().getMessage();
                                                    Toast.makeText(getApplicationContext(),"Error 1:"+error,Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        });

                            }
                        }
                        else
                        {
                            String error=task.getException().getMessage();
                            Toast.makeText(getApplicationContext(),"Error 2:"+error,Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        txtfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(txtfav.getText().toString().equals("Favourite"))
                {
                        Map<String, Object> data = new HashMap<>();
                        data.put("user_id", cust_id);
                        data.put("partner_name", partName.getText().toString());
                    firebaseFirestore.collection("Favourites")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    heart.setTextColor(Color.RED);
                                    txtfav.setText("Added");
                                    Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_LONG).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }else {

                    firebaseFirestore.collection("Favourites")
                            .whereEqualTo("partner_name", partName.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {


                                            docid = documentSnapshot.getId();
                                            firebaseFirestore.collection("Favourites").document(docid)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            heart.setTextColor(Color.BLACK);
                                                            txtfav.setText("Favourite");
                                                            Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });

                                        }


                                    }

                                }
                            });





                }
            }
        });


        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(PartnerDetails.this,BillPayment.class);
                intent.putExtra("booking_id",book_id);
                intent.putExtra("partner_name",partName.getText().toString());
                startActivity(intent);
            }
        });


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(PartnerDetails.this,BookingDetails.class);
                intent.putExtra("partner_name",partName.getText().toString());
                intent.putExtra("booking_id",book_id);
                intent.putExtra("partner_id",partner_id);

                startActivity(intent);



            }
        });

       name = getIntent().getStringExtra("partnername");
        firebaseFirestore.collection("PARTNER LIST")
                .whereEqualTo("partnerName",name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                            {

                                partName.setText(documentSnapshot.get("partnerName").toString());
                                partAddress.setText( documentSnapshot.get("partnerAddress").toString());
                                partRating.setText(documentSnapshot.get("rating").toString()+"("+documentSnapshot.get("ratingCount").toString()+")");

                        //        partTimings.setText(documentSnapshot.get("timings").toString());
                                partCuisines.setText(documentSnapshot.get("partnerCuisines").toString());
                                partCost.setText(documentSnapshot.get("partnerCost").toString());
                                partFullAddress.setText(documentSnapshot.get("partnerFullAddress").toString());
                                partDescription.setText(documentSnapshot.get("partnerDescription").toString());
                                partPhoneNo.setText(documentSnapshot.get("partnerPhoneNo").toString());
                                partner_id=documentSnapshot.get("partner_id").toString();

                            }


                        }
                        else
                        {
                            String error=task.getException().getMessage();
                        }

                    }
                });





    }
    private String getUID()
    {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        UID = new StringBuilder((100000 + rnd.nextInt(900000)) + "-");
        for (int i = 0; i < 5; i++)
            UID.append(chars[rnd.nextInt(chars.length)]);

        return UID.toString();

    }





}
