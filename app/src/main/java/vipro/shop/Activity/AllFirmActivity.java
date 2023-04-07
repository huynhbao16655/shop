package vipro.shop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vipro.shop.Adapter.FirmAdapter;
import vipro.shop.Model.FirmModel;
import vipro.shop.Model.GridSpacingItemDecoration;
import vipro.shop.Model.Server;
import vipro.shop.R;

public class AllFirmActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView AllFirmRecycler;
    FirmAdapter firmAdapter;
    ArrayList<FirmModel> allFirmModelList;

    ImageView backAllFirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_firm);
        setControl();
        setFirmRecycler();
        loadDataAllFirm();
        backAllFirm.setOnClickListener(this);
    }

    private void loadDataAllFirm() {
        RequestQueue queue = Volley.newRequestQueue(this);
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlFirmProduct, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    allFirmModelList.add(
                            new FirmModel(
                                    jsonObject.getString("code"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("image")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            firmAdapter.notifyDataSetChanged();
        }, error -> {

        });
        queue.add(jsonArrayRequest);
    }

    private void setControl() {
        AllFirmRecycler = findViewById(R.id.all_firm);
        backAllFirm = findViewById(R.id.backAllFirmProduct);
    }

    private void setFirmRecycler() {
        allFirmModelList=new ArrayList<>();
        AllFirmRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        AllFirmRecycler.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(), true));
        AllFirmRecycler.setItemAnimator(new DefaultItemAnimator());
        firmAdapter = new FirmAdapter(this,R.layout.item_firm,allFirmModelList);
        AllFirmRecycler.setAdapter(firmAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (id == R.id.backAllFirmProduct) {
            Intent back = new Intent(AllFirmActivity.this, MainActivity.class);
            startActivity(back);
            finish();
        }
    }
    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics()));
    }
}