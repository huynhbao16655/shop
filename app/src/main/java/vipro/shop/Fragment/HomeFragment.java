package vipro.shop.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import vipro.shop.Activity.AllCategoryActivity;
import vipro.shop.Activity.ProductsOfTypeActivity;
import vipro.shop.Adapter.CategoryAdapter;
import vipro.shop.Adapter.DiscountedProductAdapter;
import vipro.shop.Adapter.RecentlyAdapter;
import vipro.shop.Adapter.SliderAdapter;
import vipro.shop.Model.CategoryModel;
import vipro.shop.Model.GridSpacingItemDecoration;
import vipro.shop.Model.ProductModel;
import vipro.shop.Model.Server;
import vipro.shop.R;
public class HomeFragment extends Fragment implements View.OnClickListener {

    Context context;
    RecyclerView discountRecyclerView, categoryRecyclerView, recentlyViewedRecycler;
    DiscountedProductAdapter discountedProductAdapter;
    ArrayList<ProductModel> discountedProductsList;

    CategoryAdapter categoryAdapter;
    ArrayList<CategoryModel> categoryList;

    RecentlyAdapter recentlyViewedAdapter;
    ArrayList<ProductModel> recentlyViewedList;

    TextView allCategoryMore;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    SliderAdapter sliderAdapter;
    ArrayList listImage;
    Timer timer;
    Toolbar toolbar;
    SearchView searchView;

    public HomeFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl(view);
        addDataDiscounted();
        addDataCategory();
        addDataRecently();
//banner
        setAdapterSlider();
        loadImageSlider();
        toolbar.setNavigationIcon(R.drawable.ic_baseline_home_24);
        setDiscountedRecycler();
        setCategoryRecycler();
        setRecentlyViewedRecycler();
        allCategoryMore.setOnClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(context, ProductsOfTypeActivity.class);
                intent.putExtra("check", false);
                intent.putExtra("query", query);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void loadImageSlider() {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlBanner, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {

                    listImage.add(response.getString(i));
                    //Log.d("duy", "onResponse: "+response.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            sliderAdapter.notifyDataSetChanged();
            autoSliderImage();

        }, error -> {

        });
        queue.add(jsonArrayRequest);
    }

    private void setAdapterSlider() {
        listImage = new ArrayList();
        sliderAdapter = new SliderAdapter(context, R.layout.item_slider_image, listImage);
        viewPager.setAdapter(sliderAdapter);
        circleIndicator.setViewPager(viewPager);
        sliderAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }

    private void addDataRecently() {
        recentlyViewedList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        @SuppressLint("NotifyDataSetChanged")
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlProductsRecently, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    recentlyViewedList.add(
                            new ProductModel(
                                    jsonObject.getString("code"),
                                    jsonObject.getString("name"),
                                    jsonObject.getLong("price"),
                                    jsonObject.getInt("quantity"),
                                    jsonObject.getLong("price_discounted"),
                                    jsonObject.getString("description"),
                                    jsonObject.getString("image"),
                                    jsonObject.getString("date_update"),
                                    jsonObject.getString("type_code")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            recentlyViewedAdapter.notifyDataSetChanged();
        }, error -> {

        });
        queue.add(jsonArrayRequest);
    }

    private void addDataCategory() {
        categoryList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        @SuppressLint("NotifyDataSetChanged")
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlTypeProduct, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    categoryList.add(
                            new CategoryModel(
                                    jsonObject.getString("code"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("image")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            categoryAdapter.notifyDataSetChanged();
        }, error -> {

        });
        queue.add(jsonArrayRequest);
    }

    private void addDataDiscounted() {
        discountedProductsList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlProductsDiscounted, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    discountedProductsList.add(
                            new ProductModel(
                                    jsonObject.getString("code"),
                                    jsonObject.getString("name"),
                                    jsonObject.getLong("price"),
                                    jsonObject.getInt("quantity"),
                                    jsonObject.getLong("price_discounted"),
                                    jsonObject.getString("description"),
                                    jsonObject.getString("image"),
                                    jsonObject.getString("date_update"),
                                    jsonObject.getString("type_code")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            discountedProductAdapter.notifyDataSetChanged();
        }, error -> {

        });
        queue.add(jsonArrayRequest);
    }

    private void setControl(View view) {
        discountRecyclerView = view.findViewById(R.id.discountedRecycler);
        categoryRecyclerView = view.findViewById(R.id.categoryRecycler);
        allCategoryMore = view.findViewById(R.id.allCategoryMore);
        recentlyViewedRecycler = view.findViewById(R.id.recently_item);
        viewPager = view.findViewById(R.id.viewPager);
        circleIndicator = view.findViewById(R.id.circleindicator);
        toolbar = view.findViewById(R.id.toolbarmain);
        searchView = view.findViewById(R.id.searchview);

    }

    private void autoSliderImage() {
        if (listImage == null || listImage.size() == 0)
            return;
        if (timer == null)
            timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    int currentItem = viewPager.getCurrentItem();
                    int totalItem = listImage.size() - 1;
                    if (currentItem < totalItem) {
                        currentItem++;
                        viewPager.setCurrentItem(currentItem);
                    } else
                        viewPager.setCurrentItem(0);
                });
            }
        }, 500, 5000);
    }
    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics()));
    }
    private void setDiscountedRecycler() {
        discountedProductAdapter = new DiscountedProductAdapter(context, R.layout.item_discounted, discountedProductsList);
        discountRecyclerView.addItemDecoration(new GridSpacingItemDecoration(discountedProductsList.size()-1,dpToPx(),false));
        discountRecyclerView.setAdapter(discountedProductAdapter);
        discountRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }


    private void setCategoryRecycler() {
        categoryAdapter = new CategoryAdapter(context, R.layout.item_category, categoryList);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.addItemDecoration(new GridSpacingItemDecoration(categoryList.size()-1,dpToPx(),false));
        categoryRecyclerView.setAdapter(categoryAdapter);

    }

    private void setRecentlyViewedRecycler() {
        recentlyViewedRecycler.setLayoutManager(new GridLayoutManager(context, 2));
        recentlyViewedRecycler.addItemDecoration(new GridSpacingItemDecoration(2,dpToPx(),false));

        recentlyViewedAdapter = new RecentlyAdapter(context, R.layout.item_recently, recentlyViewedList);
        recentlyViewedRecycler.setAdapter(recentlyViewedAdapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.allCategoryMore) {
            Intent intent = new Intent(context, AllCategoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}