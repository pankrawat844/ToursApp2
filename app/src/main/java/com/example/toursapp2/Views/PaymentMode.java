package com.example.toursapp2.Views;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.toursapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaymentMode extends AppCompatActivity {

    TextView txtpayableamount;

    int stramount;
    ConstraintLayout orderconfirmationlayout;
    TextView txtorderid,txtamount,txtcustid,txtdatetime;
    Button btn_paytm;
    String partnername,custname,custnumber;
    String doc_id;
    String cust_id;
    String booking_id;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mode);

        txtpayableamount=findViewById(R.id.txt_payable_amount);
        btn_paytm=findViewById(R.id.btn_paytm);

        cust_id = FirebaseAuth.getInstance().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name  = profile.getDisplayName();
                String email = profile.getEmail();
            }
        }


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

                                doc_id=documentSnapshot.getId();


                            }


                        }
                        else
                        {

                        }

                    }
                });










        partnername=getIntent().getStringExtra("partner_name");
        booking_id=getIntent().getStringExtra("booking_id");
        stramount=getIntent().getIntExtra("final_amount",0);



        orderconfirmationlayout =findViewById(R.id.transaction_complete_layout);
        txtorderid=findViewById(R.id.txt_order_id);
        txtamount=findViewById(R.id.text_paytm_amount);
        txtcustid=findViewById(R.id.text_cust_id);
        txtdatetime=findViewById(R.id.text_date_time);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
        final String currentDateandTime = sdf.format(new Date());


        txtpayableamount.setText(stramount+" Rs");

        btn_paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(PaymentMode.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PaymentMode.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                }



                final String M_id = "IOFeuD57682242748286";
                final String order_id = UUID.randomUUID().toString().substring(0, 28);
                String url = "https://brandmorembazarr.000webhostapp.com/Paytm_App_Checksum_Kit_PHP-master/generateChecksum.php";
                final String callbackurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

                RequestQueue requestQueue= Volley.newRequestQueue(PaymentMode.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.has("CHECKSUMHASH")){
                                String CHECKSUMHASH=jsonObject.getString("CHECKSUMHASH");

                                PaytmPGService paytmPGService=PaytmPGService.getStagingService();
                                HashMap<String, String> paramMap = new HashMap<String, String>();
                                paramMap.put("MID", M_id);
                                paramMap.put("ORDER_ID", order_id);
                                paramMap.put("CUST_ID", cust_id);
                                paramMap.put("CHANNEL_ID", "WAP");
                                paramMap.put("TXN_AMOUNT",txtpayableamount.getText().toString().substring(0,txtpayableamount.getText().length()-3));
                                paramMap.put("WEBSITE", "WEBSTAGING");
                                paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                                paramMap.put("CALLBACK_URL", callbackurl);
                                paramMap.put("CHECKSUMHASH",CHECKSUMHASH);

                                PaytmOrder order=new PaytmOrder(paramMap);


                                paytmPGService.initialize(order,null);
                                paytmPGService.startPaymentTransaction(PaymentMode.this, true, true, new PaytmPaymentTransactionCallback() {
                                    @Override
                                    public void onTransactionResponse(Bundle inResponse) {
                                        Toast.makeText(getApplicationContext(), "Payment Transaction response ", Toast.LENGTH_LONG).show();

                                        if( inResponse.getString("STATUS").equals("TXN_SUCCESS")){


                                            txtorderid.setText("Order ID :"+order_id);
                                            txtcustid.setText("Customer ID: "+cust_id);
                                            txtdatetime.setText("Transaction Date and Time: "+currentDateandTime);
                                            txtamount.setText("Transaction amount "+txtpayableamount.getText().toString());
                                            orderconfirmationlayout.setVisibility(View.VISIBLE);




                                            Map<String, Object> data = new HashMap<>();

                                            data.put("order_id",order_id);
                                            data.put("transaction_time",currentDateandTime);
                                            data.put("transaction_amount",txtpayableamount.getText().toString());
                                            data.put("cust_id",cust_id);
                                            data.put("cust_name",custname);
                                            data.put("cust_number",custnumber);
                                            data.put("partner_name",partnername);


                                            firebaseFirestore.collection("Transactions")
                                                    .add(data)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {


                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });



                                        }

                                    }

                                    @Override
                                    public void networkNotAvailable() {
                                        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();


                                    }

                                    @Override
                                    public void clientAuthenticationFailed(String inErrorMessage) {
                                        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();


                                    }

                                    @Override
                                    public void someUIErrorOccurred(String inErrorMessage) {
                                        Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();


                                    }

                                    @Override
                                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                        Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();


                                    }

                                    @Override
                                    public void onBackPressedCancelTransaction() {
                                        Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                        Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();


                                    }
                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(PaymentMode.this,"Something went Wrong",Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("MID", M_id);
                        paramMap.put("ORDER_ID", order_id);
                        paramMap.put("CUST_ID", cust_id);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT", txtpayableamount.getText().toString().substring(0,txtpayableamount.getText().length()-3));
                        paramMap.put("WEBSITE", "WEBSTAGING");
                        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                        paramMap.put("CALLBACK_URL", callbackurl);
                        return paramMap;
                    }

                };
                requestQueue.add(stringRequest);





            }
        });






    }
}
