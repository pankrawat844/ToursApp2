package com.example.toursapp2.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toursapp2.Adapter.TransactionAdapter;
import com.example.toursapp2.Model.TransactionsModel;
import com.example.toursapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TransactionHistory extends AppCompatActivity {

    RecyclerView transactionrecycler;
    TransactionAdapter transactionAdapter;
    FirebaseFirestore firebaseFirestore;

    TextView uid,txtdocid,txtwallet;

    ArrayList<TransactionsModel> transactionsModelArrayList;

    String userid,docid;
    String wallet_balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Add New Product");
        txtwallet=findViewById(R.id.txt_wallet_bal);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        uid=findViewById(R.id.txt_uid_trans);
        txtdocid=findViewById(R.id.txt_docid);
        transactionrecycler=findViewById(R.id.recycle_transaction);
        firebaseFirestore=FirebaseFirestore.getInstance();
        transactionsModelArrayList=new ArrayList<>();

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        transactionrecycler.setLayoutManager(layoutManager);

        transactionAdapter=new TransactionAdapter(transactionsModelArrayList);
        transactionrecycler.setAdapter(transactionAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            userid = user.getUid();
            uid.setText(userid);


        }

        firebaseFirestore.collection("USERS")
                .whereEqualTo("cust_id", userid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                wallet_balance = documentSnapshot.get("cust_wallet").toString();
                                txtwallet.setText("₹"+wallet_balance);
                            }


                        } else {

                        }

                    }
                });


        firebaseFirestore.collection("Transactions")
                .whereEqualTo("cust_id",uid.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                            {
                                transactionsModelArrayList.add(new TransactionsModel(documentSnapshot.get("transaction_time").toString(),documentSnapshot.get("partner_name").toString(),documentSnapshot.get("transaction_amount").toString()));

                            }
                            transactionAdapter.notifyDataSetChanged();
//temp

                        }
                        else
                        {
                            String error=task.getException().getMessage();
                            Toast.makeText(TransactionHistory.this,error,Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
    }


