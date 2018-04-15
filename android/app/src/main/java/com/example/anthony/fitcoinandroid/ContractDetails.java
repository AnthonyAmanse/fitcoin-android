package com.example.anthony.fitcoinandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class ContractDetails extends AppCompatActivity {

    TextView contractId, productName, quantity, state, totalPrice;
    ImageView productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contract_details);

        contractId = findViewById(R.id.contractId);
        productName = findViewById(R.id.productNameInContract);
        quantity = findViewById(R.id.quantityInContract);
        state = findViewById(R.id.stateInContract);
        totalPrice = findViewById(R.id.totalPriceInContract);
        productImage = findViewById(R.id.productImageInContract);

        ContractModel contractModel = new Gson().fromJson(getIntent().getStringExtra("CONTRACT_JSON"),ContractModel.class);

        // Set the images based on the productId
        // images are stored in app (res/drawable*)
        // in the future, backend maybe?
        if (contractModel.getProductId().equals("eye_sticker") || contractModel.getProductId().equals("eye-sticker")) {
            productImage.setImageResource(R.drawable.eye_sticker);
            productImage.setTag(R.drawable.eye_sticker);
        } else if (contractModel.getProductId().equals("bee_sticker") || contractModel.getProductId().equals("bee-sticker")) {
            productImage.setImageResource(R.drawable.bee_sticker);
            productImage.setTag(R.drawable.bee_sticker);
        } else if (contractModel.getProductId().equals("em_sticker") || contractModel.getProductId().equals("em-sticker")) {
            productImage.setImageResource(R.drawable.em_sticker);
            productImage.setTag(R.drawable.em_sticker);
        } else {
            productImage.setImageResource(R.drawable.ic_footprint);
            productImage.setTag(R.drawable.ic_footprint);
        }

        contractId.setText(contractModel.getContractId());
        productName.setText(contractModel.getProductName());
        quantity.setText(String.valueOf(contractModel.getQuantity()));
        state.setText(contractModel.getState());
        totalPrice.setText(String.valueOf(contractModel.getCost()));

    }
}
