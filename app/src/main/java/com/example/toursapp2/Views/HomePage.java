package com.example.toursapp2.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;

import com.example.toursapp2.Adapter.PartnerItemAdapter;
import com.example.toursapp2.Model.PartnerItemModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.MotionEvent;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.toursapp2.Adapter.GridProductLayoutAdapter;
import com.example.toursapp2.Adapter.SliderAdapter;
import com.example.toursapp2.Model.HorizontalProductScrollModel;
import com.example.toursapp2.Model.SliderModel;
import com.example.toursapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    GridProductLayoutAdapter gridProductLayoutAdapter;
    private List<HorizontalProductScrollModel> categorylist;
    FirebaseFirestore firebaseFirestore;
    GridView gridView;
    ImageView heart;


    private ViewPager bannerSliderviewpager;
    private int currentpage=2;
    BottomNavigationView bottomNavigationView;

    SliderAdapter sliderAdapter;
    private List<SliderModel> slidermodellist;

    RecyclerView recyclerView;
    PartnerItemAdapter partnerItemAdapter;
    ArrayList<PartnerItemModel> partnerlist;

    final private long DELAY_TIME = 3000;
    final private long PERIOD_TIME = 3000;
    private Timer timer;
   String cust_id;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView=findViewById(R.id.partner_recycle_list_home);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        partnerlist = new ArrayList<PartnerItemModel>();
        partnerItemAdapter=new PartnerItemAdapter(this, partnerlist);
        recyclerView.setAdapter(partnerItemAdapter);

        Intent intent = getIntent();

        String city = getIntent().getStringExtra("city");

        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("PARTNER LIST")
                .whereEqualTo("city",city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                            {
                                partnerlist.add(new PartnerItemModel(documentSnapshot.get("partnerName").toString(),documentSnapshot.get("icon").toString(),(long)documentSnapshot.get("rating"),(long)documentSnapshot.get("ratingCount"),documentSnapshot.get("partnerAddress").toString(),(long)documentSnapshot.get("offerPercent"),documentSnapshot.get("partner_id").toString()  ));


                            }
                            partnerItemAdapter.notifyDataSetChanged();

                        }
                        else
                        {
                            String error=task.getException().getMessage();
                        }

                    }
                });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        bottomNavigationView = findViewById(R.id.btn_navigate);

        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {



                } else if (id == R.id.subs) {
                    Intent intent=new Intent(HomePage.this,FavouritesList.class);
                    startActivity(intent);

                } else if (id == R.id.wallet) {
                    Intent intent=new Intent(HomePage.this,TransactionHistory.class);
                    startActivity(intent);

                }
                return true;
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();


        gridView = findViewById(R.id.grid_product_view);
        categorylist = new ArrayList<>();


        firebaseFirestore.collection("CATEGORIES")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                categorylist.add(new HorizontalProductScrollModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("catname").toString(), (long) documentSnapshot.get("catid")));

                            }
                            gridProductLayoutAdapter.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(HomePage.this, error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        gridProductLayoutAdapter = new GridProductLayoutAdapter(categorylist, this);
        gridView.setAdapter(gridProductLayoutAdapter);




        bannerSliderviewpager = findViewById(R.id.banner_view_pager);

        slidermodellist = new ArrayList<SliderModel>();

        firebaseFirestore.collection("TOP_DEALS").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                long no_of_banners = (long) documentSnapshot.get("no_of_banners");
                                for (long x = 1; x < no_of_banners + 1; x++) {
                                    slidermodellist.add(new SliderModel(documentSnapshot.get("banner_" + x).toString()));
                                }


                            }

                            sliderAdapter.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(HomePage.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }


                });

        sliderAdapter = new SliderAdapter(slidermodellist, HomePage.this      );


        bannerSliderviewpager.setAdapter(sliderAdapter);
        bannerSliderviewpager.setClipToPadding(false);
        bannerSliderviewpager.setPageMargin(20);

        bannerSliderviewpager.setCurrentItem(currentpage);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {
                currentpage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == ViewPager.SCROLL_STATE_IDLE) {
                    pageLooper();
                }
            }
        };
        bannerSliderviewpager.addOnPageChangeListener(onPageChangeListener);
        startbannerSlideShow();

        bannerSliderviewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                pageLooper();
                stopbannerslideshow();

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    startbannerSlideShow();
                }
                return false;
            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_notify) {
            //todo:add search and notify ids

        }else if (id == R.id.main_search) {

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_prof) {
        //todo:Add ids of menu
        }else if (id == R.id.nav_cred) {

        }else if (id == R.id.nav_promo) {

        }else if (id == R.id.nav_fav) {

        }else if (id == R.id.nav_refer) {

        }else if (id == R.id.nav_notif) {

        }else if (id == R.id.nav_share) {

        }else if (id == R.id.nav_help) {

            sp = getSharedPreferences("otp",MODE_PRIVATE);
            sp.edit().putBoolean("logged",false).apply();
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(HomePage.this,OTPValidation.class);
            startActivity(intent);
            this.finish();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void pageLooper ()
    {
        if (currentpage == slidermodellist.size() - 2) {
            currentpage = 2;
            bannerSliderviewpager.setCurrentItem(currentpage, false);
        }
        if (currentpage == 1) {
            currentpage = slidermodellist.size() - 3;
            bannerSliderviewpager.setCurrentItem(currentpage, false);
        }
    }
    private void startbannerSlideShow () {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentpage >= slidermodellist.size()) {
                    currentpage = 1;
                }
                bannerSliderviewpager.setCurrentItem(currentpage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_TIME, PERIOD_TIME);
    }

    private void stopbannerslideshow () {
        timer.cancel();
    }


}
