package vipro.shop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vipro.shop.Activity.ProductsOfTypeActivity;
import vipro.shop.Model.FirmModel;
import vipro.shop.Model.Server;
import vipro.shop.R;

public class FirmAdapter extends RecyclerView.Adapter<FirmAdapter.FirmViewHolder> {

    Context context;
    int layout;
    ArrayList<FirmModel> firmList;

    public FirmAdapter(Context context, int layout, ArrayList<FirmModel> firmList) {
        this.context = context;
        this.layout = layout;
        this.firmList = firmList;
    }

    @NonNull
    @Override
    public FirmAdapter.FirmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_firm, parent, false);

        return new FirmAdapter.FirmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FirmAdapter.FirmViewHolder holder, int position) {
        FirmModel firmModel = firmList.get(position);
        Picasso.get().load(Server.urlImage + firmModel.getImage()).into(holder.firmImage);
        holder.firmName.setText(firmModel.getName());
        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context, ProductsOfTypeActivity.class);
            intent.putExtra("firmProduct",firmModel);
            intent.putExtra("check",true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return firmList.size();
    }

    public static class FirmViewHolder extends RecyclerView.ViewHolder {

        ImageView firmImage;
        TextView firmName;

        public FirmViewHolder(@NonNull View itemView) {
            super(itemView);
            firmImage = itemView.findViewById(R.id.firmImage);
            firmName = itemView.findViewById(R.id.firmName);
        }
    }

}
