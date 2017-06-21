package com.goldshop;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.goldshop.adapter.CartListAdapter;
import com.goldshop.adapter.CategoryInfoAdapter;
import com.goldshop.db.DB_Handler;
import com.goldshop.model.CategoryInfo;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView itemCountText;
    List<CategoryInfo> list;
    CartListAdapter mAdapter;
    Handler h;
    DB_Handler db_handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        h = new Handler();
        db_handler=new DB_Handler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CART");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemCountText= (TextView) findViewById(R.id.itemCountTextView);
        recyclerView = (RecyclerView) findViewById(R.id.cart_recyclerview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<CategoryInfo>();

        mAdapter = new CartListAdapter(this, list);
        recyclerView.setAdapter(mAdapter);

        getCartListAndRefresh();
    }


    public void getCartListAndRefresh(){
        h.post(new Runnable() {
            @Override
            public void run() {
                list.clear();
                list.addAll(db_handler.getCartData());
                mAdapter.notifyDataSetChanged();
                itemCountText.setText(list.size()+" item in your bag");
            }
        });
    }

    public void placeOrderClick(View view){
        //pending
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
                break;

        }

        return true;
    }
}
