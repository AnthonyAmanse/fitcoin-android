package com.example.anthony.fitcoinandroid;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pixplicity.sharp.Sharp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderboardsFragment extends Fragment {

    RequestQueue queue;
    Gson gson;
    String TAG = "FITNESS_LEADERBOARDS";
    String URL_TEST = "https://anthony-blockchain.us-south.containers.mybluemix.net/gravatar/new";

    public LeaderboardsFragment() {
        // Required empty public constructor
    }

    public static LeaderboardsFragment newInstance() {
        return new LeaderboardsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leaderboards, container,false);

        final ImageView userImage = rootView.findViewById(R.id.testImage);
        final TextView userName = rootView.findViewById(R.id.userName);

        gson = new Gson();

        // initialize shared preferences - persistent data
        SharedPreferences sharedPreferences = ((AppCompatActivity) getActivity()).getSharedPreferences("shared_preferences_fitcoin", Context.MODE_PRIVATE);

        // get user info from shared prefrences
        if (sharedPreferences.contains("UserInfo")) {
            String userInfoJsonString = sharedPreferences.getString("UserInfo","error");
            if (!userInfoJsonString.equals("error")) {
                Log.d(TAG, userInfoJsonString);
                UserInfoModel userInfoModel = gson.fromJson(userInfoJsonString,UserInfoModel.class);
                userImage.setImageBitmap(userInfoModel.getBitmap());
                userName.setText(userInfoModel.getName());
            }
        }

        return rootView;
    }

}
