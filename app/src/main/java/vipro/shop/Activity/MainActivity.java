package vipro.shop.Activity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vipro.shop.Fragment.CartFragment;
import vipro.shop.Fragment.HomeFragment;
import vipro.shop.Fragment.LoginSignupFragment;
import vipro.shop.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment fm;

    @Override
    //dat dieu kien xoay man hinh
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(getApplicationContext(), "Portraint Mode", Toast.LENGTH_SHORT).show();
        }else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(getApplicationContext(), "Landscape Mode", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setControl();
        boolean checkBuyNow = getIntent().getBooleanExtra("checkBuyNow", false);
        setOnClickBottomView();
        if (checkBuyNow)
            fm = new CartFragment(this);
        else {
            boolean checkPayCart = getIntent().getBooleanExtra("checkPayCart", false);
            if (checkPayCart)
                fm = new LoginSignupFragment(this, true);
            else {
                boolean checkChangpass = getIntent().getBooleanExtra("checkChangpass", false);
                if (checkChangpass)
                    fm = new LoginSignupFragment(this);
                else
                    fm = new HomeFragment(this);
            }
        }

        loadFragment(fm);

    }

    @SuppressLint("NonConstantResourceId")
    private void setOnClickBottomView() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.mn_home:
                    fm = new HomeFragment(getApplicationContext());
                    break;
                case R.id.mn_account:
                    fm = new LoginSignupFragment(getApplicationContext());
                    break;
                default:
                    fm = new CartFragment(getApplicationContext());
            }
            loadFragment(fm);
            return true;
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framehost, fragment);
        fragmentTransaction.commit();
    }

    private void setControl() {
        bottomNavigationView = findViewById(R.id.bottomnavigation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bottom, menu);
        return true;
    }

}