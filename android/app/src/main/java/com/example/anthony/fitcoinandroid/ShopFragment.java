package com.example.anthony.fitcoinandroid;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {

    ArrayList<ShopItemModel> dataModels;
    RecyclerView recyclerView;

    private static final String TAG = "FITNESS_SHOP_FRAG";
    private static final String BLOCKCHAIN_URL = "http://169.61.17.171:3000";

    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop, container, false);
//        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        actionBar.show();
//        actionBar.setTitle("Shop");

        recyclerView = rootView.findViewById(R.id.productList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        dataModels = new ArrayList<>();
        dataModels.add(new ShopItemModel("","product-1234","The Product Name",234, 2));
        dataModels.add(new ShopItemModel("","product-1235","The Product",123, 5));
        dataModels.add(new ShopItemModel("","product-1234","The Product Name",234, 2));
        dataModels.add(new ShopItemModel("","product-1235","The Product",123, 5));
        dataModels.add(new ShopItemModel("","product-1234","The Product Name",234, 2));
        dataModels.add(new ShopItemModel("","product-1235","The Product",123, 5));
        dataModels.add(new ShopItemModel("","product-1234","The Product Name",234, 2));
        dataModels.add(new ShopItemModel("","product-1235","The Product",123, 5));

        ShopItemsAdapter adapter = new ShopItemsAdapter(rootView.getContext(),dataModels);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
