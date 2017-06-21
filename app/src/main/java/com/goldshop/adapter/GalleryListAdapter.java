package com.goldshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goldshop.CategoryInfoActivity;
import com.goldshop.GalleryActivity;
import com.goldshop.R;
import com.goldshop.model.GalleryModel;

import java.util.List;

/**
 * Created by bhanwar on 13/06/2017.
 */

public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.MyViewHolder> {

    List<GalleryModel> mListData;
    GalleryActivity ctx;
    public GalleryListAdapter(GalleryActivity ctx, List<GalleryModel> mListData) {
        this.mListData = mListData;
        this.ctx=ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup,final int i) {
        View view=null;
        if(i%2==0) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_list_item,
                    viewGroup, false);
        }else{
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_list_item1,
                    viewGroup, false);
        }

        Log.d("position",""+i);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, CategoryInfoActivity.class);
                intent.putExtra("catId",mListData.get(i).getTermId());
                intent.putExtra("title",mListData.get(i).getName());
                ctx.startActivity(intent);
            }
        });
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.title.setText(mListData.get(i).getName());
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

        TextView title;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.imageNameText);
            imageView= (ImageView) itemView.findViewById(R.id.image_view);

            DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
         //   int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            imageView.getLayoutParams().height=height/3;
        }
    }

}


