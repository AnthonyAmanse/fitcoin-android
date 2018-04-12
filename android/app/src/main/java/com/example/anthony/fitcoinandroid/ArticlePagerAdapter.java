package com.example.anthony.fitcoinandroid;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticlePagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ArticleModel> articleModels;

    public ArticlePagerAdapter(Context context, ArrayList<ArticleModel> articleModels) {
        this.context = context;
        this.articleModels = articleModels;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_item, container, false);

        ArticleModel articleModel = articleModels.get(position);

        TextView articleTitle = view.findViewById(R.id.articleTitle);
        TextView articleSubTitle = view.findViewById(R.id.articleSubtitle);
        TextView articleSubtext = view.findViewById(R.id.articleSubtext);
        TextView articleDescription = view.findViewById(R.id.articleStatement);

        articleTitle.setText(articleModel.getTitle());
        articleSubTitle.setText(articleModel.getSubtitle());
        articleSubtext.setText(articleModel.getSubtext());
        articleDescription.setText(articleModel.getDescription());

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return articleModels.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
