package com.example.anthony.fitcoinandroid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<ShopItemModel> implements View.OnClickListener {

    private ArrayList<ShopItemModel> dataSet;
    Context mContext;

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        Object object = getItem(position);
        ShopItemModel dataModel = (ShopItemModel) object;
        Log.d("FITNESS", " Clicked on -- " + position + " " + dataModel.getProductName());
    }

    private static class ViewHolder {
        LinearLayout linearLayout;
        TextView productName;
        TextView productPrice;
        TextView productQuantity;
        ImageView productImage;
    }

    public CustomAdapter(ArrayList<ShopItemModel> data, Context context) {
        super(context, R.layout.shop_item, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ShopItemModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.shop_item, parent, false);
            viewHolder.linearLayout = convertView.findViewById(R.id.linearItem);
            viewHolder.productName = convertView.findViewById(R.id.productName);
            viewHolder.productPrice = convertView.findViewById(R.id.productPrice);
            viewHolder.productQuantity = (TextView) convertView.findViewById(R.id.productQuantityLeft);
            viewHolder.productImage = convertView.findViewById(R.id.productImage);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.linearLayout.setOnClickListener(this);
        viewHolder.linearLayout.setTag(position);
        viewHolder.productName.setText(dataModel.getProductName());
        viewHolder.productPrice.setText(String.valueOf(dataModel.getPrice()));
        viewHolder.productQuantity.setText(String.valueOf(dataModel.getQuantityLeft()));
        viewHolder.productImage.setTag(position);

        // TEMP
        if (position == 0) {
            viewHolder.productImage.setImageResource(R.drawable.eye_sticker);
        } else if (position == 1) {
            viewHolder.productImage.setImageResource(R.drawable.bee_sticker);
        } else if (position == 2) {
            viewHolder.productImage.setImageResource(R.drawable.em_sticker);
        } else {
            viewHolder.productImage.setImageResource(R.drawable.ic_footprint);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
