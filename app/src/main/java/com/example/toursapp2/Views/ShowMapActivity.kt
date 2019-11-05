package com.example.toursapp2.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.toursapp2.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

class ShowMapActivity : AppCompatActivity(),OnMapReadyCallback {
    private var googleMap: GoogleMap? = null

    override fun onMapReady(p0: GoogleMap?) {
        googleMap=p0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_map)

    }
}
