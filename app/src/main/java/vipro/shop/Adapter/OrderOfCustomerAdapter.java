package vipro.shop.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vipro.shop.Model.GridSpacingItemDecoration;
import vipro.shop.Model.OrderDetailModel;
import vipro.shop.Model.OrderModel;
import vipro.shop.Model.Server;
import vipro.shop.Model.Support;
import vipro.shop.R;

public class OrderOfCustomerAdapter extends RecyclerView.Adapter<OrderOfCustomerAdapter.ViewOrderOfCustomer> {
    Context context;
    int layout;
    ArrayList<OrderModel> orderModelArrayList;
    Dialog dialog;
    RecyclerView recycleviewOrderDetail;
    Button btnCancelOrderDetail;
    ArrayList<OrderDetailModel> orderDetailModelArrayList;
    OrderDetailAdapter orderDetailAdapter;

    TextView titleOrderDetail;
    String idcart;
    SharedPreferences sharedPreferencesID;

    public OrderOfCustomerAdapter(Context context, int layout, ArrayList<OrderModel> orderModelArrayList) {
        this.context = context;
        this.layout = layout;
        this.orderModelArrayList = orderModelArrayList;
    }

    @NonNull
    @Override
    public ViewOrderOfCustomer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewOrderOfCustomer(LayoutInflater.from(context).inflate(layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewOrderOfCustomer holder, @SuppressLint("RecyclerView") int position) {
        OrderModel orderModel = orderModelArrayList.get(position);
        holder.codeOrderOfCustomer.setText(orderModel.getCode());
        holder.dateOrderOfCustomer.setText(orderModel.getCreateDate());
        holder.totalStatus.setText(orderModel.getStatus());
        holder.totalDiscount.setText(Support.ConvertMoney(orderModel.getTotal()));
        holder.totalOrderOfCustomer.setText(Support.ConvertMoney(orderModel.getTotal()));
        holder.itemView.setOnClickListener(view -> openDialogOrderDeatail(position));
    }

    private void openDialogOrderDeatail(int position) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order_detail);
        dialog.setCanceledOnTouchOutside(false);
        setControl();
        setAdapterOrderDetail();
        getDataOrderDetail(position);
//        getidHoadon();
        setClick();
        dialog.show();
    }

//    private void getidHoadon() {
//        idcart = sharedPreferencesID.getString("code_order", "");
//        titleOrderDetail.setText("Chi tiết đơn hàng " + idcart);
//    }

    private void getDataOrderDetail(int position) {
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetListOrderDetailByCode + "?code_order=" + orderModelArrayList.get(position).getCode(), response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object=response.getJSONObject(i);

                    orderDetailModelArrayList.add(new OrderDetailModel(
                            object.getString("code_order"),
                            object.getString("name_product"),
                            object.getLong("price"),
                            object.getInt("quantity"),
                            object.getLong("total")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            orderDetailAdapter.notifyDataSetChanged();
        }, error -> {

        });
        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }

    private void setClick() {
        btnCancelOrderDetail.setOnClickListener(view -> dialog.dismiss());
    }

    private void setControl() {
        recycleviewOrderDetail = dialog.findViewById(R.id.recycleviewOrderDetail);
        btnCancelOrderDetail = dialog.findViewById(R.id.btnCancelOrderDetail);
    }

    private int dpToPx() {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics()));
    }

    private void setAdapterOrderDetail() {
        orderDetailModelArrayList = new ArrayList<>();
        orderDetailAdapter = new OrderDetailAdapter(context, R.layout.item_order_detail, orderDetailModelArrayList);
        recycleviewOrderDetail.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(), false));
        recycleviewOrderDetail.setAdapter(orderDetailAdapter);
        recycleviewOrderDetail.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    public static class ViewOrderOfCustomer extends RecyclerView.ViewHolder {
        TextView codeOrderOfCustomer, dateOrderOfCustomer, totalOrderOfCustomer, totalDiscount, unitTotalOrderOfCustomer1, unitTotalOrderOfCustomer2, totalStatus;

        public ViewOrderOfCustomer(@NonNull View itemView) {
            super(itemView);
            codeOrderOfCustomer = itemView.findViewById(R.id.codeOrderOfCustomer);
            dateOrderOfCustomer = itemView.findViewById(R.id.dateOrderOfCustomer);
            totalOrderOfCustomer = itemView.findViewById(R.id.totalOrderOfCustomer);
            totalDiscount = itemView.findViewById(R.id.TotalDiscount);
            totalStatus = itemView.findViewById(R.id.totalStatus);
            unitTotalOrderOfCustomer1 = itemView.findViewById(R.id.unitTotalOrderOfCustomer1);
            unitTotalOrderOfCustomer1.setPaintFlags(unitTotalOrderOfCustomer1.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            unitTotalOrderOfCustomer2 = itemView.findViewById(R.id.unitTotalOrderOfCustomer2);
            unitTotalOrderOfCustomer2.setPaintFlags(unitTotalOrderOfCustomer2.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}
