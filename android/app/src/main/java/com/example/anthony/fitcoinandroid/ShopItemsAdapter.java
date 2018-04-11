package com.example.anthony.fitcoinandroid;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        holder.cardView.setTag(shopItemModel);
        holder.cardView.setOnClickListener(this);

        holder.productName.setText(shopItemModel.getProductName());
        holder.productPrice.setText(String.valueOf(shopItemModel.getPrice()));
        holder.productQuantity.setText(String.valueOf(shopItemModel.getQuantityLeft()));

        if (position == 0) {
            holder.productImage.setImageResource(R.drawable.eye_sticker);
        } else if (position == 1) {
            holder.productImage.setImageResource(R.drawable.bee_sticker);
        } else if (position == 2) {
            holder.productImage.setImageResource(R.drawable.em_sticker);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_footprint);
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
    }

    public class ShopItemsViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productQuantity, productPrice;
        ImageView productImage;
        CardView cardView;

        public ShopItemsViewHolder(View view) {
            super(view);

            cardView = view.findViewById(R.id.shopCard);
            productName = view.findViewById(R.id.productName);
            productPrice = view.findViewById(R.id.productPrice);
            productQuantity = view.findViewById(R.id.productQuantityLeft);
            productImage = view.findViewById(R.id.productImage);
        }
    }
}
