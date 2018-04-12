package com.example.anthony.fitcoinandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class QuantitySelection extends AppCompatActivity {

    ImageView productImage;
    TextView productName;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quantity_selection);

        productImage = findViewById(R.id.productImageInQuantity);
        productName = findViewById(R.id.productNameInQuantity);

        int image = getIntent().getIntExtra("PRODUCT_CHOSEN",R.drawable.ic_footprint);
        productImage.setImageResource(image);

        String stringOfShopItemModel = getIntent().getStringExtra("PRODUCT_JSON");
        Log.d("FITNESS_QUANTITY", getIntent().getStringExtra("PRODUCT_JSON"));

        ShopItemModel shopItemModel = gson.fromJson(stringOfShopItemModel, ShopItemModel.class);
        productName.setText(shopItemModel.getProductName());
    }
}
