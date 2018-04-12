package com.example.anthony.fitcoinandroid;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ShopItemsAdapter extends RecyclerView.Adapter<ShopItemsAdapter.ShopItemsViewHolder> implements View.OnClickListener {

    private Context context;

    private ArrayList<ShopItemModel> shopItemModelList;

    public ShopItemsAdapter(Context context, ArrayList<ShopItemModel> shopItemModelList) {
        this.context = context;
        this.shopItemModelList = shopItemModelList;
    }

    @Override
    public ShopItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.shop_item, null);
        return new ShopItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopItemsViewHolder holder, int position) {
        ShopItemModel shopItemModel = shopItemModelList.get(position);

        String quantityLeftConcatenate = String.valueOf(shopItemModel.getQuantityLeft()) + " left";
        String priceConcatenate = String.valueOf(shopItemModel.getPrice()) + " coins each";

        holder.cardView.setTag(shopItemModel);
        holder.cardView.setOnClickListener(this);

        holder.productName.setText(shopItemModel.getProductName());
        holder.productPrice.setText(priceConcatenate);
        holder.productQuantity.setText(quantityLeftConcatenate);

        if (position == 0) {
            holder.productImage.setImageResource(R.drawable.eye_sticker);
            holder.productImage.setTag(R.drawable.eye_sticker);
        } else if (position == 1) {
            holder.productImage.setImageResource(R.drawable.bee_sticker);
            holder.productImage.setTag(R.drawable.bee_sticker);
        } else if (position == 2) {
            holder.productImage.setImageResource(R.drawable.em_sticker);
            holder.productImage.setTag(R.drawable.em_sticker);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_footprint);
            holder.productImage.setTag(R.drawable.ic_footprint);
        }
    }

    @Override
    public int getItemCount() {
        return shopItemModelList.size();
    }

    @Override
    public void onClick(View view) {
        ShopItemModel shopItemModel = (ShopItemModel) view.getTag();
        Log.d("FITNESS_ADAPTER_SHOP", "clicked on - " + shopItemModel.getProductName());
        ImageView productImage = (ImageView) view.findViewById(R.id.productImage);

        Intent intent = new Intent(view.getContext(), QuantitySelection.class);
        intent.putExtra("PRODUCT_CHOSEN",(Integer) productImage.getTag());
        intent.putExtra("PRODUCT_JSON", new Gson().toJson(shopItemModel, ShopItemModel.class));

//        ActivityOptions options = ActivityOptions
//                .makeSceneTransitionAnimation((Activity) view.getContext(), (ImageView) view.findViewById(R.id.productImage),"productImage");
//        options.update(ActivityOptions
//                .makeSceneTransitionAnimation((Activity) view.getContext(), (TextView) view.findViewById(R.id.productName), "productName"));

        Pair<View, String> pair1 = Pair.create(view.findViewById(R.id.productImage),"productImage");
        Pair<View, String> pair2 = Pair.create(view.findViewById(R.id.productName),"productName");

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), pair1, pair2);

        view.getContext().startActivity(intent,options.toBundle());

//        TextView text = (TextView) view.findViewById(R.id.quantityQuestion);
//
//        if (text.getVisibility() == View.GONE) {
//            text.setVisibility(View.VISIBLE);
//        } else if (text.getVisibility() == View.VISIBLE) {
//            text.setVisibility(View.GONE);
//        }
    }

    public class ShopItemsViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productQuantity, productPrice;
        TextView quantityQuestion;
        ImageView productImage;
        CardView cardView;

        public ShopItemsViewHolder(View view) {
            super(view);

            cardView = view.findViewById(R.id.shopCard);
            productName = view.findViewById(R.id.productName);
            productPrice = view.findViewById(R.id.productPrice);
            productQuantity = view.findViewById(R.id.productQuantityLeft);
            productImage = view.findViewById(R.id.productImage);
            quantityQuestion = view.findViewById(R.id.quantityQuestion);
        }
    }
}
