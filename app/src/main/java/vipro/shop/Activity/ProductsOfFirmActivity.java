package vipro.shop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vipro.shop.Adapter.ProductOfFirmAdapter;
import vipro.shop.Model.FirmModel;
import vipro.shop.Model.GridSpacingItemDecoration;
import vipro.shop.Model.ProductModel;
import vipro.shop.Model.Server;
import vipro.shop.R;
public class ProductsOfFirmActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recycleviewProductsOfFirm;
    ProductOfFirmAdapter productOfFirmAdapter;
    ArrayList<ProductModel> productModelArrayList;
    ImageView backProductOfFirm;
    TextView txtTileProductsOfFirm;
    private FirmModel firmModel;
    private boolean check;
    private String query="";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_of_firm);
        setControl();

        Intent intent = getIntent();
        check = intent.getBooleanExtra("check", true);

        if (check) {
            firmModel = (FirmModel) intent.getSerializableExtra("firmProduct");
            txtTileProductsOfFirm.setText("Danh sách sản phẩm của " + firmModel.getName());
        } else {
            query = intent.getStringExtra("query");
            txtTileProductsOfFirm.setText("Tìm kiếm theo " + query);
        }
        setRecyclerAdapter();
        loadDataProductOfFirm();
        backProductOfFirm.setOnClickListener(this);


    }

    private void loadDataProductOfFirm() {
        String url;
        if (check)
            url = Server.urlProductsOfType + "?firm_code=" + firmModel.getCode();
        else
            url = Server.urlSearch + "?keyword=" + query;
        RequestQueue queue = Volley.newRequestQueue(this);
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    productModelArrayList.add(
                            new ProductModel(
                                    jsonObject.getString("code"),
                                    jsonObject.getString("name"),
                                    jsonObject.getLong("price"),
                                    jsonObject.getInt("quantity"),
                                    jsonObject.getLong("price_discounted"),
                                    jsonObject.getString("description"),
                                    jsonObject.getString("image"),
                                    jsonObject.getString("date_update"),
                                    jsonObject.getString("type_code"),
                                    jsonObject.getString("firm_code")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            productOfFirmAdapter.notifyDataSetChanged();
        }, error -> {

        });
        queue.add(jsonArrayRequest);
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics()));
    }
    private void setRecyclerAdapter() {

        productModelArrayList = new ArrayList<>();
        productOfFirmAdapter = new ProductOfFirmAdapter(this, R.layout.item_product_of_firm, productModelArrayList);
        recycleviewProductsOfFirm.addItemDecoration(new GridSpacingItemDecoration(2,dpToPx(),false));
        recycleviewProductsOfFirm.setAdapter(productOfFirmAdapter);
        recycleviewProductsOfFirm.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void setControl() {
        recycleviewProductsOfFirm = findViewById(R.id.recycleviewProductsOfFirm);
        backProductOfFirm = findViewById(R.id.backProductOfFirm);
        txtTileProductsOfFirm = findViewById(R.id.txtTileProductsOfFirm);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.backProductOfFirm) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}