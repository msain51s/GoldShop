package com.goldshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.goldshop.CategoryInfoActivity;
import com.goldshop.ProductDetailActivity;
import com.goldshop.R;
import com.goldshop.SearchActivity;
import com.goldshop.model.CategoryInfo;
import com.goldshop.model.GalleryModel;
import com.goldshop.utility.FontType;
import com.goldshop.utility.Utils;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by bhanwar on 18/06/2017.
 */

public class CategoryInfoAdapter extends RecyclerView.Adapter<CategoryInfoAdapter.MyViewHolder> {

    List<CategoryInfo> mListData;
    Context ctx;
    String from;
    Typeface monetsarrat_regular;
    public CategoryInfoAdapter(Context ctx, List<CategoryInfo> mListData,String from) {
        this.mListData = mListData;
        this.ctx=ctx;
        this.from=from;
        monetsarrat_regular=Utils.getCustomFont(ctx, FontType.MONESTER_RAT_REGULAR);
    }

    @Override
    public CategoryInfoAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_info_list_item,
                    viewGroup, false);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Uri gmmIntentUri = Uri.parse("google.navigation:q="+mListData.get(i).getLatitude()+","+mListData.get(i).getLongitude());
              */  Intent intent = new Intent(ctx, ProductDetailActivity.class);
                  intent.putExtra("model", mListData.get(i));
                ctx.startActivity(intent);
            }
        });
        return new CategoryInfoAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryInfoAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.title.setText(mListData.get(i).getPostTitle());
        Glide.with(ctx)
                .load(mListData.get(i).getImagePath())
                .placeholder(R.drawable.place_holder)
                .into(myViewHolder.catImage);
    }

    public void removeItem(int position) {
        mListData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mListData.size());
    }
    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView catImage;
        TextView title,weight,addToBag;

        public MyViewHolder(View itemView) {
            super(itemView);

            catImage = (ImageView) itemView.findViewById(R.id.catImageView);
            title= (TextView) itemView.findViewById(R.id.catTitle);
            weight= (TextView) itemView.findViewById(R.id.catWeightText);
            addToBag= (TextView) itemView.findViewById(R.id.addToBagButton);

            title.setTypeface(monetsarrat_regular);
            addToBag.setTypeface(monetsarrat_regular);

            addToBag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
           //         Utils.showQuantityPrompt(ctx,mListData.get(getAdapterPosition()).getPostTitle(),getAdapterPosition(),"Please Enter quantity to order");

                    if(from.equalsIgnoreCase("CategoryInfo"))
                    ((CategoryInfoActivity)ctx).checkCartProduct(getAdapterPosition());
                    else if(from.equalsIgnoreCase("SearchProduct"))
                        ((SearchActivity)ctx).checkCartProduct(getAdapterPosition());
                }
            });
        }
    }

}