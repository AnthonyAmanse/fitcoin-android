package com.example.anthony.fitcoinandroid;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class QuantitySelection extends AppCompatActivity {

    ImageView productImage;
    TextView productName;
    TextView quantity;
    TextView productPrice;
    Gson gson = new Gson();
    Button incrementQuantity;
    Button decrementQuantity;
    int maxNumberInQuantity;
    Snackbar maxLimitNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quantity_selection);

        productImage = findViewById(R.id.productImageInQuantity);
        productName = findViewById(R.id.productNameInQuantity);
        productPrice = findViewById(R.id.totalPriceInQuantity);
        quantity = findViewById(R.id.quantityTextInQuantity);
        incrementQuantity = findViewById(R.id.plusButton);
        decrementQuantity = findViewById(R.id.minusButton);

        int image = getIntent().getIntExtra("PRODUCT_CHOSEN",R.drawable.ic_footprint);
        productImage.setImageResource(image);

        String stringOfShopItemModel = getIntent().getStringExtra("PRODUCT_JSON");
        Log.d("FITNESS_QUANTITY", getIntent().getStringExtra("PRODUCT_JSON"));

        final ShopItemModel shopItemModel = gson.fromJson(stringOfShopItemModel, ShopItemModel.class);
        productName.setText(shopItemModel.getProductName());
        productPrice.setText(String.valueOf(shopItemModel.getPrice()));

        maxNumberInQuantity = 3;
        maxLimitNotification = Snackbar.make(findViewById(R.id.quantityLayout),maxNumberInQuantity + " items is the maximum quantity for this product",Snackbar.LENGTH_SHORT);

        incrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.valueOf(quantity.getText().toString());
                if (number < maxNumberInQuantity) {
                    number++;
                    quantity.setText(String.valueOf(number));
                    productPrice.setText(String.valueOf(number * shopItemModel.getPrice()));
                } else {
                    if (!maxLimitNotification.isShown()) {
                        maxLimitNotification.show();
                    }
                }
            }
        });

        decrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.valueOf(quantity.getText().toString());
                if (number > 1) {
                    number--;
                    quantity.setText(String.valueOf(number));
                    productPrice.setText(String.valueOf(number * shopItemModel.getPrice()));
                }
            }
        });
    }
}
