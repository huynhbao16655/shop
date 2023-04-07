package vipro.shop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import vipro.shop.Adapter.ProductOfFirmAdapter;
import vipro.shop.Adapter.ProductOfTypeAdapter;
import vipro.shop.Model.CartModel;
import vipro.shop.Model.GridSpacingItemDecoration;
import vipro.shop.Model.ProductModel;
import vipro.shop.Model.Server;
import vipro.shop.Model.Support;
import vipro.shop.R;

public class ProductDetail2Activity extends AppCompatActivity implements View.OnClickListener {
    ImageView back, imageProductDetail;
    TextView nameProductDetail, priceProductDetail, priceDiscountProductDetail, descriptionProdcutDetail,titleSimilarProduct,unitPriceProductDetail,unitPriceDiscountProductDetail;
    EditText quantityProductDetail;
    Button btnBuyNow, btnAddCart;
    ProductModel productModel;
    SharedPreferences sharedPreferencesCart;
    ArrayList<CartModel> listCart = null;
    RecyclerView recycleviewSimilarProduct;
    ArrayList<ProductModel> lstSimilarProducts;
    ProductOfFirmAdapter similarProductAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail2);
        setControl();
        loadDataProduct();
        setClick();
        loadDataCart();
        setRecyclerAdapter();
        loadDataSimilarProduct();
    }

    private void loadDataCart() {
        sharedPreferencesCart = getSharedPreferences("cart", Context.MODE_PRIVATE);
        String cart = sharedPreferencesCart.getString("item_cart", "");
        CartModel[] cartModels = new Gson().fromJson(cart, CartModel[].class);
        if (cartModels != null)
            listCart = new ArrayList<>(Arrays.asList(cartModels));
        else
            listCart = new ArrayList<>();
    }

    private void setClick() {
        back.setOnClickListener(this);
        btnAddCart.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
    }

    private void loadDataProduct() {
        productModel = (ProductModel) getIntent().getSerializableExtra("productDetail");
        Picasso.get().load(Server.urlImage + productModel.getImage()).into(imageProductDetail);
        nameProductDetail.setText(productModel.getName());
        descriptionProdcutDetail.setText(productModel.getDescription());
        if (productModel.getPrice_discounted() > 0) {
            priceDiscountProductDetail.setText(Support.ConvertMoney(productModel.getPrice_discounted()));
            priceProductDetail.setText(Support.ConvertMoney(productModel.getPrice()));
        } else {
            priceDiscountProductDetail.setText(Support.ConvertMoney(productModel.getPrice()));
            priceProductDetail.setText("");
            unitPriceProductDetail.setText("");
        }
    }

    private void setControl() {
        back = findViewById(R.id.backProductDetail2);
        imageProductDetail = findViewById(R.id.imageProductDetail2);
        nameProductDetail = findViewById(R.id.nameProductDetail2);
        priceProductDetail = findViewById(R.id.priceProductDetail2);
        priceDiscountProductDetail = findViewById(R.id.priceDiscountProductDetail2);
        descriptionProdcutDetail = findViewById(R.id.descriptionProdcutDetail2);
        quantityProductDetail = findViewById(R.id.quantityProductDetail2);
        btnBuyNow = findViewById(R.id.btnBuyNow2);
        btnAddCart = findViewById(R.id.btnAddCart2);
        recycleviewSimilarProduct = findViewById(R.id.recycleviewSimilarProduct2);
        titleSimilarProduct = findViewById(R.id.titleSimilarProduct2);
        unitPriceProductDetail = findViewById(R.id.unitPriceProductDetail2);
        unitPriceDiscountProductDetail = findViewById(R.id.unitPriceDiscountProductDetail2);
        priceProductDetail.setPaintFlags(priceProductDetail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        unitPriceProductDetail.setPaintFlags(unitPriceProductDetail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        unitPriceDiscountProductDetail.setPaintFlags(unitPriceDiscountProductDetail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.backProductDetail:
                Intent i = new Intent(ProductDetail2Activity.this, MainActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.btnAddCart:
                if (!quantityProductDetail.getText().toString().equals(""))
                    AddCart();
                break;
            case R.id.btnBuyNow:
                if (quantityProductDetail.getText().toString().equals(""))
                    break;

                int checkAddCart = AddCart();
                if (checkAddCart == 1) {
                    Intent intent = new Intent(ProductDetail2Activity.this, MainActivity.class);
                    intent.putExtra("checkBuyNow", true);
                    startActivity(intent);
                }

                break;
        }
    }

    private int AddCart() {
        boolean check = false;
        int quantity = Integer.parseInt(quantityProductDetail.getText().toString());
        if (quantity == 0)
            return 0;
        for (CartModel item : listCart) {
            if (item.getProductModel().getCode().equals(productModel.getCode())) {
                check = true;
                if (quantity > item.getQuantityRemain()) {
                    Toast.makeText(this, "Không đủ hàng.", Toast.LENGTH_SHORT).show();
                    return -1;
                }
                listCart.remove(item);
                item.setQuantityRemain(item.getQuantityRemain() - quantity);
                item.setQuantity(item.getQuantity() + quantity);
                listCart.add(item);
                break;
            }
        }
        if (!check) {
            if (quantity > productModel.getQuantity()) {
                Toast.makeText(this, "Không đủ hàng.", Toast.LENGTH_SHORT).show();
                return -1;
            }
            listCart.add(new CartModel(productModel, quantity, productModel.getQuantity() - quantity));
        }
        Toast.makeText(this, "Thêm sản phẩm thành công.", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editorCart = sharedPreferencesCart.edit();
        editorCart.putString("item_cart", new Gson().toJson(listCart));
        editorCart.apply();
        return 1;
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics()));
    }

    private void setRecyclerAdapter() {

        lstSimilarProducts = new ArrayList<>();
        similarProductAdapter = new ProductOfFirmAdapter(this, R.layout.item_product_of_type, lstSimilarProducts);
        recycleviewSimilarProduct.setLayoutManager(new GridLayoutManager(this,2));
        recycleviewSimilarProduct.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), false));
        recycleviewSimilarProduct.setAdapter(similarProductAdapter);
    }

    private void loadDataSimilarProduct() {

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlProductsOfType + "?type_code=" + productModel.getType_code(), response -> {
            boolean check = false;
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    if (productModel.getCode().equals(jsonObject.getString("code")))
                        continue;
                    lstSimilarProducts.add(
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
            if (lstSimilarProducts.size() == 0)
                titleSimilarProduct.setText("");
            similarProductAdapter.notifyDataSetChanged();
        }, error -> {

        });
        queue.add(jsonArrayRequest);
    }


}