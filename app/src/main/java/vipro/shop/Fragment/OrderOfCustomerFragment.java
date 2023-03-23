package vipro.shop.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vipro.shop.Adapter.OrderOfCustomerAdapter;
import vipro.shop.Model.GridSpacingItemDecoration;
import vipro.shop.Model.OrderModel;
import vipro.shop.Model.Server;
import vipro.shop.R;


public class OrderOfCustomerFragment extends Fragment {
    Context context;
    RecyclerView recycleviewOrderOfCustomer;
    ArrayList<OrderModel> orderModelArrayList;
    OrderOfCustomerAdapter orderOfCustomerAdapter;
    String username;
    SharedPreferences sharedPreferencesUser;
    TextView titleOrderOfCustomer;

    public OrderOfCustomerFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_of_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl(view);
        getCustomer();
        setAdapterRecycleview();
        getDataOrderOfCustomer();
    }

    private void getCustomer() {
        sharedPreferencesUser = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sharedPreferencesUser.getString("username", "");
        titleOrderOfCustomer.setText("Danh sách đơn hàng của " + username);
    }

    private void getDataOrderOfCustomer() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetListOrderOfCustomer + "?username=" + username, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        orderModelArrayList.add(new OrderModel(object.getString("code"), object.getString("username"), object.getLong("total"), object.getString("create_date")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                orderOfCustomerAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }

    private void setAdapterRecycleview() {
        orderModelArrayList = new ArrayList<>();
        orderOfCustomerAdapter = new OrderOfCustomerAdapter(context, R.layout.item_order_of_customer, orderModelArrayList);
        recycleviewOrderOfCustomer.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(8), true));
        recycleviewOrderOfCustomer.setAdapter(orderOfCustomerAdapter);
        recycleviewOrderOfCustomer.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }

    private void setControl(View view) {
        recycleviewOrderOfCustomer = view.findViewById(R.id.recycleviewOrderOfCustomer);
        titleOrderOfCustomer = view.findViewById(R.id.titleOrderOfCustomer);
    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}