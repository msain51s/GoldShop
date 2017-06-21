package com.goldshop;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.goldshop.adapter.CategoryInfoAdapter;
import com.goldshop.adapter.GalleryListAdapter;
import com.goldshop.db.DB_Handler;
import com.goldshop.model.CategoryInfo;
import com.goldshop.model.GalleryModel;
import com.goldshop.service.Response;
import com.goldshop.service.ResponseListener;
import com.goldshop.service.ServerRequest;
import com.goldshop.utility.Connection;
import com.goldshop.utility.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryInfoActivity extends AppCompatActivity implements ResponseListener{
    RecyclerView recyclerView;
    List<CategoryInfo> list;
    CategoryInfoAdapter mAdapter;
    Handler h;
    String catId,title;

    DB_Handler db_handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_info);
        h = new Handler();
        db_handler=new DB_Handler(this);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            catId=bundle.getString("catId");
            title=bundle.getString("title");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.categoryInfo_recyclerview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<CategoryInfo>();

        mAdapter = new CategoryInfoAdapter(this, list);
        recyclerView.setAdapter(mAdapter);


        getSelectedCategoryInfo();

    }

    public void productAddToCart(int position,String quantity){
    //    Toast.makeText(this,"quantity-position"+quantity+"-"+position,Toast.LENGTH_LONG).show();
        list.get(position).setProduct_quantity(Integer.parseInt(quantity));
        db_handler.addDataToCart(list.get(position));
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

    public void onFilterClick(View view){
        Utils.showFilterPrompt(this,"FILTERS");
    }

    public void onSortClick(View view){
        Utils.showSortByPrompt(this,"SORT BY");
    }
    public void getSelectedCategoryInfo() {

        if (Utils.ChechInternetAvalebleOrNot(CategoryInfoActivity.this)) {

            Utils.showLoader(CategoryInfoActivity.this);
            ServerRequest
                    .postRequest(
                            Connection.BASE_URL + "get_all_products",
                            getCattegoryInfoData(catId),
                            CategoryInfoActivity.this,
                            ResponseListener.REQUEST_CATEGORY_INFO);

        } else {
            //   Utils.shonterwSnakeBar(layout_view, "internet not connected !!!", Color.RED);Toast.makeText(LoginActivity.this,"Internet not connected !!!",Toast.LENGTH_LONG).show();
            Toast.makeText(CategoryInfoActivity.this, "Internet not connected !!!", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public JSONObject getCattegoryInfoData(String catId) {
        JSONObject json = new JSONObject();
        try {

            json.put("catID", catId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;

    }

    @Override
    public void onResponse(final Response response, final int rid) {


        h.post(new Runnable() {

            @Override
            public void run() {
                Utils.dismissLoader();
                if (rid == ResponseListener.REQUEST_CATEGORY_INFO) {

                    if (response.isError()) {
                        Toast.makeText(CategoryInfoActivity.this, response.getErrorMsg(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.getData() != null) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response.getData());
                            JSONObject jsonObject = null;
                            JSONArray jsonArray = null;
                            String status = jsonObject1.getString("status");
                            if (status.equalsIgnoreCase("true")) {
                                jsonArray = jsonObject1.getJSONArray("record");
                            }
                            CategoryInfo model = null;
                            list.clear();

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    jsonObject = jsonArray.getJSONObject(i);
                                    model = new CategoryInfo();
                                    model.setCatID(jsonObject.getInt("ID"));
                                    model.setPostTitle(jsonObject.getString("post_title"));
                                    model.setPostDate(jsonObject.getString("post_date"));
                                    model.setPostContent(jsonObject.getString("post_content"));
                                    model.setPostExcerpt(jsonObject.getString("post_excerpt"));
                                    model.setPostStatus(jsonObject.getString("post_status"));
                                    model.setPostName(jsonObject.getString("post_name"));
                                    model.setPostModifiedDate(jsonObject.getString("post_modified"));
                                    model.setPostType(jsonObject.getString("post_type"));
                                    model.setImage(jsonObject.getString("image"));
                                    model.setImagePath(jsonObject.getString("imagePath"));
                                    model.setParent(jsonObject.getString("parent"));
                                    model.setTermTaxonomyID(jsonObject.getString("term_taxonomy_id"));

                                    list.add(model);
                                }
                                //          getSupportActionBar().setTitle("Booking ("+booking_list.size()+")");
                                //          recyclerView.setAdapter(new GalleryListAdapter(GalleryActivity.this,list));
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(CategoryInfoActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_LONG).show();
                            }

                            Log.d("json_response", response.getData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
    }

}