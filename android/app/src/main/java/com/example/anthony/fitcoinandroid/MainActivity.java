package com.example.anthony.fitcoinandroid;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FITNESS_API";
    private static final String BLOCKCHAIN_URL = "http://169.61.17.171:3000";
    public RequestQueue queue;
    final Handler handler = new Handler();
    Gson gson = new Gson();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_technology:
                    selectedFragment = TechFragment.newInstance();
                    break;
                case R.id.navigation_user:
                    selectedFragment = UserFragment.newInstance();
                    break;
                case R.id.navigation_shop:
                    selectedFragment = ShopFragment.newInstance();
                    break;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // use tech fragment - initial layout
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, TechFragment.newInstance());
        transaction.commit();

        // request queue
        queue = Volley.newRequestQueue(this);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "access fine location not yet granted");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        // initialize shared preferences - persistent data
        sharedPreferences = this.getSharedPreferences("shared_preferences_fitcoin", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Check if user is already enrolled
        if (sharedPreferences.contains("BlockchainUserId")) {
            Log.d(TAG, "User already registered.");
        } else {
            try {
                // register the user
                registerUser();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerUser() throws JSONException {
        JSONObject params = new JSONObject("{\"type\":\"enroll\",\"queue\":\"user_queue\",\"params\":{}}");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BLOCKCHAIN_URL + "/api/execute", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        Log.d(TAG, "Response is: " + response.toString());
                        try {
                            Log.d(TAG, "ResultId is: " + response.get("resultId"));
                            getResultFromResultId("enrollment", response.get("resultId").toString(),0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "That didn't work!");
            }
        });

        this.queue.add(jsonObjectRequest);
    }

    public void getResultFromResultId(final String initialRequestType, final String resultId, final int attemptNumber) {
        // Limit to 60 attempts
        if (attemptNumber < 60) {
            if (initialRequestType.equals("enrollment")) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BLOCKCHAIN_URL + "/api/results/" + resultId, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Display the first 500 characters of the response string.
                                Log.d(TAG, "Response is: " + response.toString());
                                BackendResult backendResult = gson.fromJson(response.toString(), BackendResult.class);
                                if (backendResult.status.equals("pending")) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getResultFromResultId(initialRequestType,resultId,attemptNumber + 1);
                                        }
                                    },3000);
                                } else if (backendResult.status.equals("done")) {
                                    saveUser(backendResult.result);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "That didn't work!");
                    }
                });
                this.queue.add(jsonObjectRequest);
            }
        } else {
            Log.d(TAG, "No results after 60 times...");
        }
    }

    public void saveUser(String result) {
        ResultOfEnroll resultOfEnroll = gson.fromJson(result, ResultOfEnroll.class);
        Log.d(TAG, resultOfEnroll.result.user);
        editor.putString("BlockchainUserId",resultOfEnroll.result.user);
        editor.apply();
    }

}
