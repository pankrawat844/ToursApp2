package com.example.toursapp2.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toursapp2.Adapter.PartnerItemAdapter;
import com.example.toursapp2.Model.PartnerItemModel;
import com.example.toursapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class FavouritesList extends AppCompatActivity {
    RecyclerView recyclerView;
    PartnerItemAdapter partnerItemAdapter;
    ArrayList<PartnerItemModel> partnerlist;
    FirebaseFirestore firebaseFirestore;
    String user,partname;
    TextView addfav;
    ImageView favicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_list);

        addfav=findViewById(R.id.txt_fav);
        favicon=findViewById(R.id.img_fav);
        recyclerView=findViewById(R.id.partner_recycle_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        partnerlist = new ArrayList<PartnerItemModel>();
        partnerItemAdapter=new PartnerItemAdapter(this, partnerlist);
        recyclerView.setAdapter(partnerItemAdapter);
        user = FirebaseAuth.getInstance().getUid();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Add New Product");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        favicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FavouritesList.this,HomePage.class);
                startActivity(intent);
                FavouritesList.this.finish();
            }
        });

        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Favourites")
                .whereEqualTo("user_id",user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                            {

                                partname= documentSnapshot.get("partner_name").toString();

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
                                                        addfav.setVisibility(View.INVISIBLE);
                                                        favicon.setVisibility(View.INVISIBLE);
                                                        partnerlist.add(new PartnerItemModel(documentSnapshot.get("partnerName").toString(),documentSnapshot.get("icon").toString(),(long)documentSnapshot.get("rating"),(long)documentSnapshot.get("ratingCount"),documentSnapshot.get("partnerAddress").toString(),(long)documentSnapshot.get("offerPercent"),documentSnapshot.get("partner_id").toString()));

                                                    }
                                                    partnerItemAdapter.notifyDataSetChanged();

                                                }
                                                else
                                                {
                                                   ;
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





    }
}
