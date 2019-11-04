package com.example.toursapp2.Views;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.toursapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class BillPayment extends AppCompatActivity {



    String cust_id;
    FirebaseFirestore firebaseFirestore;
    long wallet_balance;
    int finalamount;

    TextView txtwalletbalance,txttotalbill,txtfinalamount;
    EditText editbillamount;

    Button btnproceedpayment;
    String partnername,booking_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment);

        cust_id = FirebaseAuth.getInstance().getUid();
        txtwalletbalance=findViewById(R.id.txt_wallet_credits);
        editbillamount=findViewById(R.id.edit_bill_amount);
        txttotalbill=findViewById(R.id.txt_total_bill);
        txtfinalamount=findViewById(R.id.txt_final_amount);
        btnproceedpayment=findViewById(R.id.btn_proceed_payment);
        booking_id=getIntent().getStringExtra("booking_id");
       partnername=getIntent().getStringExtra("partner_name");


        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("USERS")
                .whereEqualTo("cust_id",cust_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                            {


                                wallet_balance=(long)documentSnapshot.get("cust_wallet");
                                txtwalletbalance.setText(""+wallet_balance);

                            }


                        }
                        else
                        {

                        }

                    }
                });


        editbillamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                txttotalbill.setText(editbillamount.getText().toString()+" Rs");

                int totalbill= Integer.parseInt(editbillamount.getText().toString());
                finalamount=totalbill-200;
                txtfinalamount.setText(finalamount+" Rs");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btnproceedpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BillPayment.this,PaymentMode.class);
                intent.putExtra("final_amount",finalamount);
                intent.putExtra("partner_name",partnername);
                intent.putExtra("booking_id",booking_id);
                startActivity(intent);
            }
        });

    }

}
