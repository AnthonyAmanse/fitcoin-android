package com.example.anthony.fitcoinandroid;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TechFragment extends Fragment {

    ArrayList<ArticleModel> articles;
    ViewPager viewPager;
    Gson gson = new Gson();

    public TechFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View rootView = inflater.inflate(R.layout.fragment_tech, container, false);

        viewPager = rootView.findViewById(R.id.articlePager);

        articles = new ArrayList<>();

        articles.add(gson.fromJson("{\"page\":1,\"link\":\"https://developer.ibm.com/code/2018/03/16/building-a-secret-map-an-experiment-in-the-economy-of-things/\",\"title\":\"test\",\"subtitle\":\"2018\",\"image\":\"boy pirate\",\"subtext\":\"secret map\",\"description\":\"an IBM Code experiment\"}", ArticleModel.class));
        articles.add(gson.fromJson("{\"page\":2,\"link\":\"https://developer.ibm.com/code/patterns/use-mongoose-and-mongodb-to-serve-app-data/\",\"title\":\"qwer\",\"subtitle\":\"to the future\",\"image\":\"map\",\"subtext\":\"find your way\",\"description\":\"we built this indoor map to help you find a way around the expo … and to show cloud in action\"}",ArticleModel.class));
        articles.add(gson.fromJson("{\"page\":3,\"link\":\"https://developer.ibm.com/code/patterns/use-mongoose-and-mongodb-to-serve-app-data/\",\"title\":\"asdf\",\"subtitle\":\"to the future\",\"image\":\"map\",\"subtext\":\"find your way\",\"description\":\"we built this indoor map to help you find a way around the expo … and to show cloud in action\"}",ArticleModel.class));
        articles.add(gson.fromJson("{\"page\":4,\"link\":\"https://developer.ibm.com/code/patterns/use-mongoose-and-mongodb-to-serve-app-data/\",\"title\":\"zxcv\",\"subtitle\":\"to the future\",\"image\":\"map\",\"subtext\":\"find your way\",\"description\":\"we built this indoor map to help you find a way around the expo … and to show cloud in action\"}",ArticleModel.class));
        articles.add(gson.fromJson("{\"page\":5,\"link\":\"https://developer.ibm.com/code/patterns/use-mongoose-and-mongodb-to-serve-app-data/\",\"title\":\"qwer\",\"subtitle\":\"to the future\",\"image\":\"map\",\"subtext\":\"find your way\",\"description\":\"we built this indoor map to help you find a way around the expo … and to show cloud in action\"}",ArticleModel.class));

        ArticlePagerAdapter adapter = new ArticlePagerAdapter(rootView.getContext(), articles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        return rootView;
    }

    public static TechFragment newInstance() {
        return new TechFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
