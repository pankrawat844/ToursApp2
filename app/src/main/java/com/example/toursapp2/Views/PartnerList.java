package com.example.toursapp2.Views;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toursapp2.Adapter.PartnerItemAdapter;
import com.example.toursapp2.Model.PartnerItemModel;
import com.example.toursapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class PartnerList extends AppCompatActivity {

    RecyclerView recyclerView;
    PartnerItemAdapter partnerItemAdapter;
    ArrayList<PartnerItemModel> partnerlist;
    FirebaseFirestore firebaseFirestore;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_list);
        recyclerView=findViewById(R.id.partner_recycle_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        partnerlist = new ArrayList<PartnerItemModel>();
        partnerItemAdapter=new PartnerItemAdapter(this, partnerlist);
        recyclerView.setAdapter(partnerItemAdapter);

        Intent intent = getIntent();

        long number = getIntent().getExtras().getLong("catid");

        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("PARTNER LIST")
                .whereEqualTo("catid",number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                            {
                                partnerlist.add(new PartnerItemModel(documentSnapshot.get("partnerName").toString(),documentSnapshot.get("icon").toString(),(long)documentSnapshot.get("rating"),(long)documentSnapshot.get("ratingCount"),documentSnapshot.get("partnerAddress").toString(),(long)documentSnapshot.get("offerPercent"),documentSnapshot.get("partner_id").toString()));


                            }
                            partnerItemAdapter.notifyDataSetChanged();

                        }
                        else
                        {
                            String error=task.getException().getMessage();
                      }

                    }
                });





    }
}
