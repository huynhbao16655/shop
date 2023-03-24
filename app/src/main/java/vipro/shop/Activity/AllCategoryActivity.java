package vipro.shop.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vipro.shop.Adapter.CategoryAdapter;
import vipro.shop.Model.CategoryModel;
import vipro.shop.Model.GridSpacingItemDecoration;
import vipro.shop.Model.Server;
import vipro.shop.R;


public class AllCategoryActivity extends AppCompatActivity  implements View.OnClickListener {
    RecyclerView AllCategoryRecycler;
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryModel> allCategoryModelList;

    ImageView backAllCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        setControl();
        setCategoryRecycler();
        loadDataAllCategory();
        backAllCategory.setOnClickListener(this);
    }

    private void loadDataAllCategory() {
        RequestQueue queue = Volley.newRequestQueue(this);
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlTypeProduct, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    allCategoryModelList.add(new CategoryModel(jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getString("image")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            categoryAdapter.notifyDataSetChanged();
        }, error -> {

        });
        queue.add(jsonArrayRequest);
    }

    private void setControl() {
        AllCategoryRecycler = findViewById(R.id.all_category);
        backAllCategory = findViewById(R.id.backAllTypeProduct);
    }

    private void setCategoryRecycler() {
        allCategoryModelList=new ArrayList<>();
        AllCategoryRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        AllCategoryRecycler.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(), true));
        AllCategoryRecycler.setItemAnimator(new DefaultItemAnimator());
        categoryAdapter = new CategoryAdapter(this,R.layout.item_category,allCategoryModelList);
        AllCategoryRecycler.setAdapter(categoryAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (id == R.id.backAllTypeProduct) {
            Intent back = new Intent(AllCategoryActivity.this, MainActivity.class);
            startActivity(back);
            finish();
        }
    }
    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics()));
    }
}