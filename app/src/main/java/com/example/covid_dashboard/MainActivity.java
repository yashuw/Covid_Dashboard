package com.example.covid_dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import java.util.List;
import java.util.Locale;

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

public class MainActivity extends AppCompatActivity {

    private Button btn_Worldwide, btn_district, btn_prediction;
    public static TextView tv_Location;
    public static TextView tv_totalcases, tv_activecases, tv_totalrecovery, tv_totaldeaths,tv_newrecovery,tv_newdeaths;
    String data = "";
    String url = "https://api.covid19api.com/summary";
    String result = "";
    String Country_Loc;
    private RequestQueue mQueue;
    String userCountry = "unkonown ";
    public static JSONArray jsonArray;
    LocationManager locationManager;
    LocationListener locationListener;
    FusedLocationProviderClient fusedLocationProviderClient;

    public static ArrayList<String> CountryName;
    public static ArrayList<String> CountryData;

    public static ArrayList<GlobalDataFetch>Country_Data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        btn_Worldwide = (Button) findViewById(R.id.Worldwide_btn);
        btn_district = (Button) findViewById(R.id.district_btn);
        btn_prediction = (Button) findViewById(R.id.prediction_btn);
        tv_Location = (TextView) findViewById(R.id.Location_tv);
        tv_totalcases = (TextView) findViewById(R.id.totalcase_tv);
        tv_activecases = (TextView) findViewById(R.id.activecases_tv);
        tv_totalrecovery = (TextView) findViewById(R.id.totalrecovery_tv);
        tv_totaldeaths = (TextView) findViewById(R.id.totaldeaths_tv);
        tv_newdeaths=(TextView)findViewById(R.id.newdeath_tv);
        tv_newrecovery=(TextView)findViewById(R.id.newrecovery_tv);
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
        } else {
            Toast.makeText(this, "In else ", Toast.LENGTH_SHORT).show();
            //Initialize fusedLocation provider
            fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
            if(ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                getLocation();
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            }


            mQueue = Volley.newRequestQueue(this);


            btn_Worldwide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

        }

    }

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
                        Toast.makeText(MainActivity.this, "Country Name : "+addresses.get(0).getCountryName(), Toast.LENGTH_SHORT).show();

                        String Locality=addresses.get(0).getAdminArea();

                        String subloc=addresses.get(0).getSubAdminArea();

                        Toast.makeText(MainActivity.this, "Admin Area : "+Locality, Toast.LENGTH_SHORT).show();

                        Toast.makeText(MainActivity.this, "sub Admin Area : "+subloc, Toast.LENGTH_SHORT).show();
                        jsonParse();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

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
                                if (CountryCode.equals(userCountry)) {

                                    tv_Location.setText(Country.toUpperCase());
                                    tv_activecases.setText(data.getString("NewConfirmed"));


                                    tv_totalcases.setText(data.getString("TotalConfirmed"));


                                    tv_totaldeaths.setText(data.getString("TotalDeaths"));


                                    tv_totalrecovery.setText(data.getString("TotalRecovered"));


                                    tv_newdeaths.setText(data.getString("NewDeaths"));


                                    tv_newrecovery.setText(data.getString("NewRecovered"));


                                }
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

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}
