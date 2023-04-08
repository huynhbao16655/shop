package vipro.shop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vipro.shop.Activity.ProductDetailActivity;
import vipro.shop.Model.ProductModel;
import vipro.shop.Model.Server;
import vipro.shop.Model.Support;
import vipro.shop.R;
public class ProductOfFirmAdapter extends RecyclerView.Adapter<ProductOfFirmAdapter.ViewProductOfFirm> {
    Context context;
    int layout;
    ArrayList<ProductModel> arrayList;


    public ProductOfFirmAdapter(Context context, int layout, ArrayList<ProductModel> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewProductOfFirm onCreateViewHolder(@NonNull ViewGroup parent, int viewFirm) {
        return new ViewProductOfFirm(LayoutInflater.from(context).inflate(layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProductOfFirm holder, int position) {
        ProductModel productModel=arrayList.get(position);
        Picasso.get().load(Server.urlImageproduct+productModel.getImage()).into(holder.imageProductOfFirm);
        holder.nameProductOfFirm.setText(productModel.getName());
        if(productModel.getPrice_discounted()>0)
        {
            holder.priceSaleProductOfFirm.setText(Support.ConvertMoney(productModel.getPrice_discounted()));
            holder.priceProductOfFirm.setText(Support.ConvertMoney(productModel.getPrice()));
            holder.unitPriceProductOfFirm.setText("đ");
        }
        else
        {
            holder.unitPriceProductOfFirm.setText("");
            holder.priceProductOfFirm.setText("");
            holder.priceSaleProductOfFirm.setText(Support.ConvertMoney(productModel.getPrice()));
        }
        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context, ProductDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("productDetail",productModel);
            context.startActivity(intent);
            ((Activity)context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewProductOfFirm extends RecyclerView.ViewHolder{

        ImageView imageProductOfFirm;
        TextView nameProductOfFirm,priceSaleProductOfFirm,unitPriceSaleProductOfFirm
                ,unitPriceProductOfFirm,priceProductOfFirm;

        public ViewProductOfFirm(@NonNull View itemView) {
            super(itemView);
            imageProductOfFirm = itemView.findViewById(R.id.imageProductOfFirm);
            nameProductOfFirm = itemView.findViewById(R.id.nameProductOfFirm);
            priceSaleProductOfFirm=itemView.findViewById(R.id.priceSaleProductOfFirm);
            unitPriceSaleProductOfFirm=itemView.findViewById(R.id.unitPriceSaleProductOfFirm);
            unitPriceProductOfFirm=itemView.findViewById(R.id.unitPriceProductOfFirm);
            priceProductOfFirm=itemView.findViewById(R.id.priceProductOfFirm);
            unitPriceProductOfFirm.setPaintFlags(unitPriceProductOfFirm.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            unitPriceSaleProductOfFirm.setPaintFlags(unitPriceSaleProductOfFirm.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            priceProductOfFirm.setPaintFlags(priceProductOfFirm.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        }
    }
}
