package com.example.anthony.fitcoinandroid;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private static final String TAG = "FITNESS_API_USER_FRAG";
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;

    private static OnDataPointListener stepListener;

    public TextView userSteps;
    public TextView distanceFromSteps;
    public TextView userId;

    private long userStartingDate;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("Footsteps");

        // attach labels
        userSteps = rootView.findViewById(R.id.numberOfSteps);
        distanceFromSteps = rootView.findViewById(R.id.distance);
        userId = rootView.findViewById(R.id.userIdText);

        // initialize shared preferences - persistent data
        SharedPreferences sharedPreferences = ((AppCompatActivity) getActivity()).getSharedPreferences("shared_preferences_fitcoin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // check if enrolled in blockchain network
        if (sharedPreferences.contains("BlockchainUserId")) {
            userId.setText(sharedPreferences.getString("BlockchainUserId","Something went wrong..."));
        } else {
            userId.setText(R.string.notEnrolled);
        }

        // Get the date now
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        // if DateStarted is existing, use it
        if (sharedPreferences.contains("DateStarted")) {
            userStartingDate = sharedPreferences.getLong("DateStarted", cal.getTimeInMillis());
        } else { // else use time and date now and persist it for later use
            userStartingDate = cal.getTimeInMillis();
            editor.putLong("DateStarted",userStartingDate);
            editor.apply();
        }

        // build the fitness options
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        // check if app has permissions
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount((AppCompatActivity) getActivity()), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount((AppCompatActivity) getActivity()),
                    fitnessOptions);
        } else {
            accessGoogleFit();
        }

        return rootView;
    }

    private void accessGoogleFit() {
        // Subscribe to recordings
        Fitness.getRecordingClient((AppCompatActivity) getActivity(), GoogleSignIn.getLastSignedInAccount((AppCompatActivity) getActivity()))
                .subscribe(DataType.AGGREGATE_STEP_COUNT_DELTA)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully subscribed");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "There was a problem subscribing...", e);
                    }
                });

        Fitness.getRecordingClient((AppCompatActivity) getActivity(), GoogleSignIn.getLastSignedInAccount((AppCompatActivity) getActivity()))
                .subscribe(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully subscribed");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "There was a problem subscribing...", e);
                    }
                });
        Fitness.getRecordingClient((AppCompatActivity) getActivity(), GoogleSignIn.getLastSignedInAccount((AppCompatActivity) getActivity()))
                .subscribe(DataType.TYPE_DISTANCE_DELTA)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully subscribed");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "There was a problem subscribing...", e);
                    }
                });
        // end of subscriptions

        // prepare to get history
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        long startTime = userStartingDate;

        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        Log.d(TAG,"Range Start: " + dateFormat.format(startTime));
        Log.d(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(365, TimeUnit.DAYS)
                .build();

        // get history
        Fitness.getHistoryClient((AppCompatActivity) getActivity(), GoogleSignIn.getLastSignedInAccount((AppCompatActivity) getActivity()))
                .readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        Log.d(TAG, "successfully got history");
                        printData(dataReadResponse);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "failed to get history", e);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
                Log.d(TAG, "accessing...");
                accessGoogleFit();
            }
        }
    }

    public void printData(DataReadResponse dataReadResult) {

        // number of buckets would always be 1 (bucket size was set to 365 days in readRequest)
        if (dataReadResult.getBuckets().size() > 0) {
            Log.d(TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
        }
    }


    // Logic for reading the data sets.
    private void dumpDataSet(DataSet dataSet) {
        Log.d(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = DateFormat.getTimeInstance();

        int totalStepsFromDataPoints = 0;
        float distanceTraveledFromDataPoints = 0;

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.d(TAG, "Data point:");
            Log.d(TAG, "\tType: " + dp.getDataType().getName());
            Log.d(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.d(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));

            for (Field field : dp.getDataType().getFields()) {
                Log.d(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));

                // increment the steps or distance
                if (field.getName().equals("steps")) {
                    totalStepsFromDataPoints += dp.getValue(field).asInt();
                } else if (field.getName().equals("distance")) {
                    distanceTraveledFromDataPoints += dp.getValue(field).asFloat();
                }
            }
        }

        // update the proper labels
        if (dataSet.getDataType().getName().equals("com.google.step_count.delta")) {
            userSteps.setText(String.valueOf(totalStepsFromDataPoints));
        } else if (dataSet.getDataType().getName().equals("com.google.distance.delta")) {
            distanceFromSteps.setText(String.format("%.2f", distanceTraveledFromDataPoints));
        }
    }

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Resumed app");

        // initialize the step listener
        stepListener = new OnDataPointListener() {
            @Override
            public void onDataPoint(DataPoint dataPoint) {
                for (Field field : dataPoint.getDataType().getFields()) {
                    Log.d(TAG, "Field: " + field.getName());
                    Log.d(TAG, "Value: " + dataPoint.getValue(field));

                    if (field.getName().equals("steps")) {
                        int currentSteps = Integer.valueOf(userSteps.getText().toString());
                        currentSteps = currentSteps + dataPoint.getValue(field).asInt();
                        userSteps.setText(Integer.toString(currentSteps));
                    }

                }
            }
        };

        // register the step listener
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount((AppCompatActivity) getActivity()))) {
            Log.d(TAG, "Not signed in...");
        } else {
            Fitness.getSensorsClient((AppCompatActivity) getActivity(), GoogleSignIn.getLastSignedInAccount((AppCompatActivity) getActivity()))
                    .add(
                            new SensorRequest.Builder()
                                    .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                                    .setSamplingRate(3, TimeUnit.SECONDS)
                                    .build(), stepListener

                    )
                    .addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Listener Registered.");
                                    } else {
                                        Log.e(TAG, "Listener not registered", task.getException());
                                    }
                                }
                            }
                    );
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // unregister the listener
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount((AppCompatActivity) getActivity()))) {
            Log.d(TAG, "Not signed in...");
        } else {
            Fitness.getSensorsClient((AppCompatActivity) getActivity(), GoogleSignIn.getLastSignedInAccount((AppCompatActivity) getActivity()))
                    .remove(stepListener)
                    .addOnCompleteListener(
                            new OnCompleteListener<Boolean>() {
                                @Override
                                public void onComplete(@NonNull Task<Boolean> task) {
                                    if (task.isSuccessful() && task.getResult()) {
                                        Log.d(TAG, "Listener for steps was removed.");
                                    } else {
                                        Log.e(TAG, "Listener for steps was not removed.", task.getException());
                                    }
                                }
                            }
                    );
        }
    }
}
