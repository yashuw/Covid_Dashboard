package com.example.covid_dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button btn_Worldwide, btn_district, btn_prediction,btn_testcenter;
    public static TextView tv_Location;
    public static TextView tv_totalcases, tv_activecases, tv_totalrecovery, tv_totaldeaths,tv_newrecovery,tv_newdeaths;
    public static String Locality;
    String data = "";
    String url = "https://api.covid19api.com/summary";
    String result = "";
    String Country_Loc;
    private RequestQueue mQueue;
    private RequestQueue dQueue;
    String userCountry = "unkonown ";
    public static JSONArray jsonArray;
    LocationManager locationManager;
    LocationListener locationListener;
    FusedLocationProviderClient fusedLocationProviderClient;
    String subloc;

    public static ArrayList<GlobalDataFetch>Country_Data;

    DatabaseReference ref;
    public static Map<String,ArrayList<String>> map;

    public static Map<String,JSONObject>DistrictDataFetch;

    //Progress Dialog
    ProgressDialog progressDialog;


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        btn_Worldwide = (Button) findViewById(R.id.Worldwide_btn);
        btn_district = (Button) findViewById(R.id.district_btn);
        btn_prediction = (Button) findViewById(R.id.prediction_btn);
        btn_testcenter=(Button)findViewById(R.id.testcenters_btn);
        tv_Location = (TextView) findViewById(R.id.Location_tv);
        tv_totalcases = (TextView) findViewById(R.id.totalcase_tv);
        tv_activecases = (TextView) findViewById(R.id.activecases_tv);
        tv_totalrecovery = (TextView) findViewById(R.id.totalrecovery_tv);
        tv_totaldeaths = (TextView) findViewById(R.id.totaldeaths_tv);
//        tv_newdeaths=(TextView)findViewById(R.id.newdeath_tv);
//        tv_newrecovery=(TextView)findViewById(R.id.newrecovery_tv);
        Country_Data=new ArrayList<>();


        if (!isConnected()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Internet Connection")
                    .setMessage("Please check ur connection")
                    .setPositiveButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
        else {

            //Toast.makeText(this, "In else ", Toast.LENGTH_SHORT).show();

            //---------------------------------------Permission Check -------------------------------------------

            //------------------Initialize fusedLocation provider----------------------

            fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
            if((ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED))
            {
                //---------------------------------------Progress Bar Start Function -------------------------------------
                StartingProgressBar();
                getLocation();

           }
            else {
                //Toast.makeText(this, "In second else ", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            }



            //----------------------------District Data------------------------------------------------

            //Initializing map for storing District Data
            DistrictDataFetch=new HashMap<String, JSONObject>();

            //Volley Request for fetching District json data
            dQueue=Volley.newRequestQueue(this);

            //Initialising the map and database ref
            map=new HashMap<String,ArrayList<String>>();



            //----------------------------Country Wise Data  ----------------------------------------------

            //Volley Request for fetching global json data
            mQueue = Volley.newRequestQueue(this);


            //Country Wise Data Fetch Function Call
            jsonParse();

            //----------------------------Test Centers Data-------------------------------------------------

            ref= FirebaseDatabase.getInstance().getReference().child("Test Centers");
            //TestCenter Data Fetch Fuction Call
            TestCenterData();


            //-----------------------------Button Click Listeners-------------------------------------------

            btn_Worldwide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(this, "Data Parse : ", Toast.LENGTH_SHORT).show();


                    Intent WorldwideIntent = new Intent(MainActivity.this, Recycler_View.class);
                    startActivity(WorldwideIntent);
                }
            });
            btn_district.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent DistrictIntent = new Intent(MainActivity.this, District.class);
                    startActivity(DistrictIntent);


                }
            });
            btn_testcenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent testcenter = new Intent(MainActivity.this, TestCenter.class);
                    startActivity(testcenter);
                }
            });

        }

    }
    //-------------------------------------------Progress Bar -------------------------------------------

    private void StartingProgressBar() {
        //Intialising Progress Dialog
        progressDialog=new ProgressDialog(MainActivity.this);

        //show Dialog
        progressDialog.show();

        //set content view
        progressDialog.setContentView(R.layout.progress_dialog);

        //Set Transparent Background

        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        progressDialog.setCancelable(false);
    }

    //----------------------------------------User Current Loc Function ---------------------------------
    private void getLocation()
    {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location=task.getResult();
                if(location!=null)
                {
                    try {
                        Geocoder geocoder=new Geocoder(MainActivity.this,Locale.getDefault());
                        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        userCountry=addresses.get(0).getCountryCode();
                        //et_Location.setText(addresses.get(0).getCountryName());
                       // Toast.makeText(MainActivity.this, "Country Name : "+addresses.get(0).getCountryName(), Toast.LENGTH_SHORT).show();

                        Locality=addresses.get(0).getAdminArea().toUpperCase().trim();

                        subloc=addresses.get(0).getSubAdminArea().trim();


                        //Toast.makeText(MainActivity.this, "Admin Area : "+Locality, Toast.LENGTH_SHORT).show();

                        //Toast.makeText(MainActivity.this, "sub Admin Area  : "+subloc, Toast.LENGTH_SHORT).show();

                        //District Wise Data Fetch Function Call
                        jsonDistrict();



                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //----------------------------------------------------JSON DATA FETCH----------------------------------------


    //-------------------------------------------Country Wise Data Fetch Function--------------------------------

    private void jsonParse() {
        String url = "https://api.covid19api.com/summary";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray("Countries");
                            String date = (String) response.get("Date");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                String Country = data.getString("Country").toUpperCase();
                                String TotalCase=data.getString("TotalConfirmed");
                                String NewCases=data.getString("NewConfirmed");
                                String NewRecovery=data.getString("NewRecovered");
                                String TotalRecovery=data.getString("TotalRecovered");
                                String NewDeaths=data.getString("NewDeaths");
                                String TotalDeaths=data.getString("TotalDeaths");
                                String CountryCode = data.getString("CountryCode");
                                Country_Data.add( new GlobalDataFetch(Country,TotalCase,NewCases,NewRecovery,TotalRecovery,NewDeaths,TotalDeaths));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    //-------------------------------------------------Test Center Data Fetch Function-----------------------------------

    private void TestCenterData()
    {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String State=ds.getKey().toLowerCase().trim();
                    ArrayList<String>temp=new ArrayList<>();
                    //temp=map.get(State);
                    for(DataSnapshot ds1:ds.getChildren())
                    {

                        String Test_center= (String) ds1.getValue();
                        String Area=ds1.getKey();
                        String value=(Test_center+" , ")+Area;
                        // Toast.makeText(TestCenter.this, " Test Center  : "+value, Toast.LENGTH_SHORT).show();
                       // Toast.makeText(MainActivity.this, "Area : " + Area, Toast.LENGTH_SHORT).show();
                        temp.add(value);
                    }
                    map.put(State,temp);
                    //Toast.makeText(MainActivity.this, "State : "+State, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, "Value : "+map.get(State), Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(MainActivity.this, "Value : "+map.get(Locality), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //-------------------------------State Wise Data Fetch Function -----------------------------------------------

    private void jsonDistrict()
    {
        String url="https://api.covid19india.org/state_district_wise.json";
        JsonObjectRequest req=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Iterator<String> Keys=response.keys();
                        //Toast.makeText(MainActivity.this, "Keys Size : ", Toast.LENGTH_SHORT).show();
                        String temp=Keys.next();
                        while(Keys.hasNext())
                        {
                            String StateName=Keys.next().toString();
                            String Name=StateName.toUpperCase();
                            //Toast.makeText(MainActivity.this, "Keys : "+StateName, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject jsobj=response.getJSONObject(StateName);
                                JSONObject StateData=jsobj.getJSONObject("districtData");
                                Iterator<String>keys=StateData.keys();
                                //Toast.makeText(MainActivity.this, "Sub Keys : "+keys.next(), Toast.LENGTH_SHORT).show();
                                DistrictDataFetch.put(Name,StateData);
//                                JSONObject StateDat=DistrictDataFetch.get(StateName.toUpperCase());
//                                Iterator<String>key=StateDat.keys();
//                                Toast.makeText(MainActivity.this, "keys : "+key.next(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //Toast.makeText(MainActivity.this, "locality : "+Locality, Toast.LENGTH_SHORT).show();
                        JSONObject CurrentStateData=DistrictDataFetch.get(Locality);
                       // Toast.makeText(MainActivity.this, "sublocality : "+subloc, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject CurrentdistData=CurrentStateData.getJSONObject(subloc);
                            String DistrictName=subloc.toUpperCase();
                            String Confirmed=CurrentdistData.getString("confirmed");
                            String Active=CurrentdistData.getString("active");
                            String Deceased=CurrentdistData.getString("deceased");
                            String Recovered= CurrentdistData.getString("recovered");
                           progressDialog.setCancelable(true);
                            progressDialog.dismiss();
                            tv_Location.setText(DistrictName);
                            tv_activecases.setText(Active);


                            tv_totalcases.setText(Confirmed);


                            tv_totaldeaths.setText(Deceased);


                            tv_totalrecovery.setText(Recovered);

                            } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        dQueue.add(req);
    }

    //--------------------------------------Network connected Check Function----------------------------------

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}
