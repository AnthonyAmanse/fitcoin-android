package com.example.anthony.fitcoinandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ContractList extends AppCompatActivity {

    Gson gson = new Gson();
    ArrayList<ContractModel> contractModels;
    RecyclerView recyclerView;
    ContractListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contracts_list);

        String contractModelsJson = getIntent().getStringExtra("CONTRACT_MODELS_JSON");

        recyclerView = findViewById(R.id.contractsList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contractModels = new ArrayList<>();

        ContractModel[] contractModelsReceived = gson.fromJson(contractModelsJson, ContractModel[].class);

        // reverse the contracts so that newest show at the top
        List<ContractModel> temp = Arrays.asList(contractModelsReceived);
        Collections.reverse(temp);
        contractModels.addAll(temp);

        // attach adapter to view
        adapter = new ContractListAdapter(this, contractModels);
        recyclerView.setAdapter(adapter);


    }
}
