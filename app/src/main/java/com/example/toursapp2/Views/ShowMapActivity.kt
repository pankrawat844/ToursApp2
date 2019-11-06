package com.example.toursapp2.Views

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.Toast
import com.example.toursapp2.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.koushikdutta.ion.Ion
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.IOException


class ShowMapActivity : AppCompatActivity(),OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private val TAG: String = "ShowMapActivity"
    internal var marker: MarkerOptions? = null
    internal var PickupLarLng: LatLng? = null
    internal var DropLarLng: LatLng? = null
    internal var DropLongtude: Double = 0.toDouble()
    internal var DropLatitude: Double = 0.toDouble()
    internal var PickupLongtude: Double = 0.toDouble()
    internal var PickupLatitude: Double = 0.toDouble()
    internal var PickupMarker: Marker? = null
    internal var DropMarker: Marker? = null
    internal var gpsTracker: GPSTrackerKt? = null
    var listLatLng = ArrayList<LatLng?>()

    override fun onMapReady(p0: GoogleMap?) {
        googleMap=p0


        DropLarLng = getLocationFromAddress(intent.getStringExtra("address"))
        DropLongtude =  DropLarLng!!.longitude
        DropLatitude = DropLarLng!!.latitude

        MarkerAdd()
        CaculationDirationIon()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_map)

        gpsTracker = GPSTrackerKt(this@ShowMapActivity)
        if (gpsTracker!!.checkLocationPermission()) {

            PickupLatitude = gpsTracker!!.latitude
            PickupLongtude = gpsTracker!!.longitude
            PickupLarLng = LatLng(gpsTracker!!.latitude, gpsTracker!!.longitude)

            if (isNetworkAvailable(this@ShowMapActivity)) {
             /*   val locationAddress = LocationAddress
                locationAddress.getAddressFromLocation(
                        PickupLatitude, PickupLongtude,
                        applicationContext, GeocoderHandler()
                )*/

                PickupLarLng = LatLng(PickupLatitude, PickupLongtude)

                Handler().postDelayed({ MarkerAdd() }, 1000)
            } else {
                Toast.makeText(this@ShowMapActivity, "No Network", Toast.LENGTH_LONG).show()
            }

        } else {
            gpsTracker!!.showSettingsAlert()
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    private fun checkReady(): Boolean {
        if (googleMap == null) {
            Toast.makeText(this, "Google Map not ready", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    fun MarkerAdd() {
    if (checkReady()) {

        if (marker != null)
            googleMap!!.clear()

        val builder = LatLngBounds.Builder()


        if (PickupLarLng != null) {
            marker = MarkerOptions()
                    .position(PickupLarLng!!)
                    .title("Pick Up Location")

                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup_location_icon))
            PickupMarker = googleMap!!.addMarker(marker)
            PickupMarker!!.isDraggable = true
            builder.include(marker!!.position)


        }

        if (DropLarLng != null) {
            marker = MarkerOptions()
                    .position(DropLarLng!!)
                    .title("Drop Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_location_icon))

            DropMarker = googleMap!!.addMarker(marker)
            DropMarker!!.isDraggable = true
            builder.include(marker!!.position)
        }

        // .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon))

        if (DropLarLng != null || PickupLarLng != null) {
            val bounds = builder.build()

            //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10));
                val cu = CameraUpdateFactory.newLatLngBounds(bounds,50)
                googleMap!!.animateCamera(cu, object : GoogleMap.CancelableCallback {

                    override fun onFinish() {
                        if (PickupMarker != null)
                            BounceAnimationMarker(PickupMarker, PickupLarLng)
                        if (DropMarker != null)
                            BounceAnimationMarker(DropMarker, DropLarLng)
                    }

                    override fun onCancel() {

                    }
                })



            }
        }


         }



    fun BounceAnimationMarker(animationMarker: Marker?, animationLatLng: LatLng?) {
        if (animationLatLng != null) {
            val handler = Handler()
            val start = SystemClock.uptimeMillis()
            val proj = googleMap!!.projection
            val startPoint = proj.toScreenLocation(animationLatLng)
            startPoint.offset(0, -100)
            val startLatLng = proj.fromScreenLocation(startPoint)
            val duration: Long = 1500
            val interpolator = BounceInterpolator()
            handler.post(object : Runnable {
                override fun run() {
                    val elapsed = SystemClock.uptimeMillis() - start
                    val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                    val lng = t * animationLatLng.longitude + (1 - t) * startLatLng.longitude
                    val lat = t * animationLatLng.latitude + (1 - t) * startLatLng.latitude
                    animationMarker!!.position = LatLng(lat, lng)
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16)
                    }
                }
            })
        }
    }


    fun  getLocationFromAddress(strAddress:String): LatLng? {

        val coder = Geocoder(this)
        try {
            val adresses = coder.getFromLocationName(strAddress, 50) as ArrayList<Address>
            for ((value,index) in  adresses.withIndex()) {
                if(value==0)  {//Controls to ensure it is right address such as country etc.
                    val longitude = index.longitude
                    val latitude = index.latitude
                    return LatLng(latitude,longitude)
                }

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun isNetworkAvailable(act: Activity): Boolean {

        val connMgr = act.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected

    }
    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded.get(index++).toInt() - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = (if ((result and 1) != 0) (result shr 1).inv() else (result shr 1))
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded.get(index++).toInt() - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = (if ((result and 1) != 0) (result shr 1).inv() else (result shr 1))
            lng += dlng
            val p = LatLng(
                    ((lat.toDouble() / 1E5)),
                    ((lng.toDouble() / 1E5))
            )
            poly.add(p)
        }
        return poly
    }
    fun AddPollyLines(list: ArrayList<LatLng?>) {
        val bounds: LatLngBounds.Builder
        if (checkReady()) {
            if (marker != null)
                googleMap!!.clear()
            if (listLatLng.size > 1) {
                var polyOption = PolylineOptions()
                polyOption.color(Color.RED)
                polyOption.width(12f)
                polyOption.addAll(list)
                googleMap!!.clear()
                googleMap!!.addPolyline(polyOption)
                bounds = LatLngBounds.builder()
                for (i in list) {
                    bounds.include(i)
                }
//
                val latLngBounds: LatLngBounds = bounds.build()
                val cameraUpdate: CameraUpdate =
                        CameraUpdateFactory.newLatLngBounds(latLngBounds, 0)

                if (PickupLarLng != null) {
                    marker = MarkerOptions()
                            .position(PickupLarLng!!)
                            .title("Pick Up Location")

                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup_location_icon))
                    PickupMarker = googleMap!!.addMarker(marker)
                    PickupMarker!!.isDraggable = true
                    bounds.include(marker!!.position)


                }

                if (DropLarLng != null) {
                    marker = MarkerOptions()
                            .position(DropLarLng!!)
                            .title("Drop Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_location_icon))

                    DropMarker = googleMap!!.addMarker(marker)
                    DropMarker!!.isDraggable = true
                    bounds.include(marker!!.position)
                }
            }
        }
    }
    fun CaculationDirationIon() {
        var CaculationLocUrl = ""
        //        try {
        //            DrowLocUrl = "http://maps.googleapis.com/maps/api/directions/json?sensor=true&mode=driving&origin="+URLEncoder.encode(edt_pickup_location.getText().toString(), "UTF-8")+"&destination="+URLEncoder.encode(edt_drop_location.getText().toString(), "UTF-8");
        //        } catch (UnsupportedEncodingException e) {
        //            e.printStackTrace();
        //        }
        listLatLng.clear()
        CaculationLocUrl =
                "https://maps.googleapis.com/maps/api/directions/json?sensor=true&mode=driving&origin=$PickupLatitude,$PickupLongtude&destination=$DropLatitude,$DropLongtude&key=" + getString(
                        R.string.google_server_key
                )
        Log.d("CaculationLocUrl", "CaculationLocUrl = $CaculationLocUrl")
        Ion.with(this@ShowMapActivity)
                .load(CaculationLocUrl)
                .setTimeout(10000)
                .asJsonObject()
                .setCallback { error, result ->
                    // do stuff with the result or error



                    Log.d("Login result", "Login result = $result==$error")
                    if (error == null) {
                        try {
                            val resObj = JSONObject(result.toString())
                            if (resObj.getString("status").toLowerCase() == "ok") {


                                val routArray = JSONArray(resObj.getString("routes"))
                                val routObj = routArray.getJSONObject(0)
                                val overview_polylines = routObj.getJSONObject("overview_polyline")
                                listLatLng.addAll(decodePoly(overview_polylines.getString("points")))
                                Log.d("geoObj", "DrowLocUrl geoObj one= $routObj")
                                val legsArray = JSONArray(routObj.getString("legs"))
                                val legsObj = legsArray.getJSONObject(0)
                                val legsSteps = legsObj.getJSONArray("steps")
//                            for (i in 0 until legsSteps.length()) {
//                                val startLocJSON = legsSteps.getJSONObject(i)
//                                val startLoc = startLocJSON.getJSONObject("start_location")
//                                val endLoc = startLocJSON.getJSONObject("end_location")
//
//                                listLatLng.add(
//                                    LatLng(
//                                        startLoc.getDouble("lat"),
//                                        startLoc.getDouble("lng")
//                                    )
//                                )
//                                listLatLng.add(
//                                    LatLng(
//                                        endLoc.getDouble("lat"),
//                                        endLoc.getDouble("lng")
//                                    )
//                                )
//
//                            }
                                AddPollyLines(listLatLng)
                                val disObj = JSONObject(legsObj.getString("distance"))
                                //if (disObj.getInt("value") > 1000)
//                                distance = disObj.getInt("value").toFloat() / 1000
                                //                                    else if (disObj.getInt("value") > 100)
                                //                                        distance = (float) disObj.getInt("value") / 100;
                                //                                    else if (disObj.getInt("value") > 10)
                                //                                        distance = (float) disObj.getInt("value") / 10;
                                //                                    else if(disObj.getInt("value") == 0)
                                //                                        distance = (float) disObj.getInt("value");
//                                Log.d("distance", "distance = " + distance!!)
//                                Log.d("dis", "dis = " + distance!!)

                                val duration = JSONObject(legsObj.getString("duration"))

//                                AstTime = duration.getString("text")
//                                val durTextSpi =
//                                        AstTime.split(" ".toRegex()).dropLastWhile({ it.isEmpty() })
//                                                .toTypedArray()
//                                Log.d("durTextSpi", "min  = durTextSpi = " + durTextSpi.size)
                                var hours = 0
                                var mintus = 0
//                                if (durTextSpi.size == 4) {
//                                    hours = Integer.parseInt(durTextSpi[0]) * 60
//                                    mintus = Integer.parseInt(durTextSpi[2])
//                                } else if (durTextSpi.size == 2) {
//                                    if (durTextSpi[1].contains("mins"))
//                                        mintus = Integer.parseInt(durTextSpi[0])
//                                    else
//                                        mintus = Integer.parseInt(durTextSpi[0])
//                                }
                                Log.d("hours", "hours = $hours==$mintus")
//                                totalTime = mintus + hours
//
//                                googleDuration = duration.getInt("value")





                            } else {
                                Toast.makeText(
                                        this@ShowMapActivity,
                                        "Something Went Wrong",
                                        Toast.LENGTH_LONG
                                ).show()
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else {
                        Toast.makeText(this@ShowMapActivity, error.message,Toast.LENGTH_SHORT).show()
                    }
                }


    }
}

