package com.example.anthony.fitcoinandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class QuantitySelection extends AppCompatActivity {

    private static final String TAG = "FITNESS_QUANTITY";
    private static final String BLOCKCHAIN_URL = "http://169.61.17.171:3000";

    ImageView productImage;
    TextView productName;
    TextView quantity;
    TextView productPrice;
    Gson gson = new Gson();
    Button incrementQuantity;
    Button decrementQuantity;
    Button claimButton;
    int maxNumberInQuantity;
    Snackbar maxLimitNotification;
    String userId;
    boolean isEnrolled;

    ShopItemModel product;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quantity_selection);

        // get the user id
        SharedPreferences sharedPreferences = this.getSharedPreferences("shared_preferences_fitcoin", Context.MODE_PRIVATE);

        // check if enrolled in blockchain network
        if (sharedPreferences.contains("BlockchainUserId")) {
            this.userId = sharedPreferences.getString("BlockchainUserId","Something went wrong...");
            if (this.userId.equals("Something went wrong...")) {
                this.isEnrolled = false;
            } else {
                this.isEnrolled = true;
            }
        } else {
            this.isEnrolled = false;
        }

        // connect views
        productImage = findViewById(R.id.productImageInQuantity);
        productName = findViewById(R.id.productNameInQuantity);
        productPrice = findViewById(R.id.totalPriceInQuantity);
        quantity = findViewById(R.id.quantityTextInQuantity);
        incrementQuantity = findViewById(R.id.plusButton);
        decrementQuantity = findViewById(R.id.minusButton);
        claimButton = findViewById(R.id.claimButton);

        // get image from chosen product
        int image = getIntent().getIntExtra("PRODUCT_CHOSEN",R.drawable.ic_footprint);
        productImage.setImageResource(image);

        // get the data model in string of the chosen product
        String stringOfShopItemModel = getIntent().getStringExtra("PRODUCT_JSON");
        Log.d("FITNESS_QUANTITY", getIntent().getStringExtra("PRODUCT_JSON"));

        // convert the string to a data model and set to views
        final ShopItemModel shopItemModel = gson.fromJson(stringOfShopItemModel, ShopItemModel.class);
        this.product = shopItemModel;

        productName.setText(shopItemModel.getProductName());
        productPrice.setText(String.valueOf(shopItemModel.getPrice()));

        // request queue
        queue = Volley.newRequestQueue(this);

        // make the max quantity and notification
        maxNumberInQuantity = 3;
        maxLimitNotification = Snackbar.make(findViewById(R.id.quantityLayout),maxNumberInQuantity + " items is the maximum quantity for this product",Snackbar.LENGTH_SHORT);

        // set on click listeners on button
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

        claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // send makePurchase request to blockchain here
                Log.d(TAG, "You have user id of: " + userId);
                Log.d(TAG, "Buy product id: " + shopItemModel.getProductId() + " and seller id: " + shopItemModel.getSellerId() + " and quantity: " + quantity.getText().toString());
                purchaseItem();
            }
        });
    }

    public void purchaseItem() {
        try {
            JSONObject params = new JSONObject("{\"type\":\"invoke\",\"queue\":\"user_queue\",\"params\":{\"userId\":\"" + userId + "\", \"fcn\":\"makePurchase\", \"args\":[" + userId + "," + product.getSellerId() + "," + product.getProductId() + ",\"" + quantity.getText() + "\"]}}");
            Log.d(TAG, params.toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BLOCKCHAIN_URL + "/api/execute", params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            InitialResultFromRabbit initialResultFromRabbit = gson.fromJson(response.toString(), InitialResultFromRabbit.class);
                            if (initialResultFromRabbit.status.equals("success")) {
                                Log.d(TAG, response.toString());
//                                getResultFromResultId("getStateOfUser",initialResultFromRabbit.resultId,0, failedAttempts);
                            } else {
                                Log.d(TAG, "Response is: " + response.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "That didn't work!");
                }
            });
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
