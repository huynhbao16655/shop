package vipro.shop.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vipro.shop.Model.CartModel;
import vipro.shop.Model.ProductModel;
import vipro.shop.Model.Server;
import vipro.shop.Model.Support;
import vipro.shop.R;
import vn.momo.momo_partner.AppMoMoLib;

public class PayOrderActivity extends AppCompatActivity {
    EditText name_order, address_order, phone_order;
    TextView sumProductConfirm, totalConfirm,unitMoneyPayOrderTotal;
    AppCompatButton btnConfirmOrder, btnMomo;
    SharedPreferences sharedPreferencesCart, sharedPreferencesUser;
    ArrayList<CartModel> lstCart;

    ArrayList<ProductModel> lstProducts;
    String username;
    ImageView backPayOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
         // AppMoMoLib.ENVIRONMENT.PRODUCTION
        setControl();
        getDataCustomer();
        getDataCart();
        setDataCart();
        setClick();
    }

    @SuppressLint("SetTextI18n")
    private void setDataCart() {
        int sum = 0;
        long total = 0;
        for (CartModel cartModel : lstCart) {
            sum += cartModel.getQuantity();
            if (cartModel.getProductModel().getPrice_discounted() > 0)
                total += cartModel.getQuantity() * cartModel.getProductModel().getPrice_discounted();
            else
                total += cartModel.getQuantity() * cartModel.getProductModel().getPrice();
        }
        sumProductConfirm.setText(sum + "");
        totalConfirm.setText(Support.ConvertMoney(total));
    }

    private void getDataCart() {
        sharedPreferencesCart = getSharedPreferences("cart", Context.MODE_PRIVATE);
        String cart = sharedPreferencesCart.getString("item_cart", "");
        CartModel[] cartModels = new Gson().fromJson(cart, CartModel[].class);
        lstCart = new ArrayList<>(Arrays.asList(cartModels));
    }

    private void getDataCustomer() {
        sharedPreferencesUser = getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sharedPreferencesUser.getString("username", "");
        RequestQueue queue = Volley.newRequestQueue(this);
        @SuppressLint("SetTextI18n") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Server.urlGetCustomerByUsername + "?username=" + username, null, response -> {
            try {
                name_order.setText(response.getString("name"));
                address_order.setText(response.getString("address"));
                phone_order.setText(0 + response.getString("phone"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        queue.add(jsonObjectRequest);
    }

    private void setClick() {
        btnConfirmOrder.setOnClickListener(view -> insertOrder());
        backPayOrder.setOnClickListener(view -> {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("checkBuyNow",true);
            startActivity(intent);
            finish();
        });

        btnMomo.setOnClickListener(view -> {
            AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
            requestPayment();
        });
    }

    private void updateOrder(String code_order) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlUpdateOrder, response -> {

        }, error -> {

        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                long total = 0;
                for (CartModel cartModel : lstCart)
                    if (cartModel.getProductModel().getPrice_discounted() > 0)
                        total += cartModel.getQuantity() * cartModel.getProductModel().getPrice_discounted();
                    else
                        total += cartModel.getQuantity() * cartModel.getProductModel().getPrice();
                params.put("total", total + "");
                params.put("code_order", code_order);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void OrderDetail(String code_order) {
        for (CartModel cartModel : lstCart) {
            insertOrderDetail(code_order, cartModel);
        }
    }

    private void insertOrderDetail(String code_order, CartModel cartModel) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlInsertOrderDetail, response -> {
        }, error -> {
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                long price;
                if(cartModel.getProductModel().getPrice_discounted()>0)
                    price=cartModel.getProductModel().getPrice_discounted();
                else
                    price=cartModel.getProductModel().getPrice();
                params.put("code_order", code_order);
                params.put("code_product", cartModel.getProductModel().getCode());
                params.put("price", price + "");
                params.put("quantity", cartModel.getQuantity() + "");
                params.put("total", price * cartModel.getQuantity() + "");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void insertOrder() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlInsertOrder, response -> {
            if (!response.equals("0")) {
                OrderDetail(response);
                updateOrder(response);
                Toast.makeText(getApplicationContext(), "Đặt hàng thành công.", Toast.LENGTH_SHORT).show();
                getSharedPreferences("cart", Context.MODE_PRIVATE)
                        .edit().clear().apply();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else
                Toast.makeText(getApplicationContext(), "Đặt hàng thất bại.", Toast.LENGTH_SHORT).show();

        }, error -> {

        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }



    private void setControl() {
        name_order = findViewById(R.id.name_order);
        address_order = findViewById(R.id.address_order);
        phone_order = findViewById(R.id.phone_order);
        sumProductConfirm = findViewById(R.id.sumProductConfirm);
        totalConfirm = findViewById(R.id.totalConfirm);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        btnMomo = findViewById(R.id.btnMomo);
        backPayOrder = findViewById(R.id.backPayOrder);
        unitMoneyPayOrderTotal = findViewById(R.id.unitMoneyPayOrderTotal);
        unitMoneyPayOrderTotal.setPaintFlags(unitMoneyPayOrderTotal.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

    }

    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        long mahd =   System.currentTimeMillis();
        eventValue.put("merchantname", "Thanh Nhan"); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", "MOMO1NRV20220112"); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", totalConfirm); //Kiểu integer
        eventValue.put("orderId", "order"+mahd); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", "Mã đơn hàng"); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", totalConfirm); //Kiểu integer
        eventValue.put("description", "Mô tả"); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  "MOMO1NRV20220112"+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", "MOMO1NRV20220112");
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "Thanh Toán Đồ Điện Tử");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Đặc Biệt");
            String name ="";
            for(ProductModel sanPham : lstProducts){
                name+=sanPham.getName()+",";
            }
            objExtraData.put("movie_name", name);
            objExtraData.put("movie_format", "Đồ điện tử");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(PayOrderActivity.this, eventValue);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CHECKED","checked1");
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            Log.d("CHECKED","checked2");
            if(data != null) {
                Log.d("CHECKED","checked3");
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    Log.d("Messagesss","message: " + "Get token " + data.getStringExtra("message"));
                    String checked = data.getStringExtra("message");
                    Log.d("CHECKED",checked);
                    Calendar calendar=Calendar.getInstance();
                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }

                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
                        Log.d("Message Error : ","message: " + "Get token " + data.getStringExtra("message"));

                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    Log.d("Message Fail : ","message: " + "Get token " + data.getStringExtra("message"));
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    Log.d("Message Fail 1 : ","message: " + "Get token " + data.getStringExtra("message"));
                } else {
                    //TOKEN FAIL
                    Log.d("Message Fail 2 : ","message: " + "Get token " + data.getStringExtra("message"));
                }
            } else {
                Log.d("Message Fail 3 : ","message: " + "Get token " + data.getStringExtra("message"));
            }
        } else {
            Log.d("Message Fail 4 : ","message: " + "Get token " + data.getStringExtra("message"));
        }
    }


}