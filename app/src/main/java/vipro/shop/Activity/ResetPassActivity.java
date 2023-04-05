package vipro.shop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import vipro.shop.Model.CategoryModel;
import vipro.shop.Model.Server;
import vipro.shop.R;

public class ResetPassActivity extends AppCompatActivity {

    EditText editEmail;
    Button btnresetpass;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        setControl();
    }
    private void setControl() {
        editEmail = findViewById(R.id.editEmail);
        btnresetpass = findViewById(R.id.btnresetpass);
    }


    private void loadDataEmail() {
        RequestQueue queue = Volley.newRequestQueue(this);
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlReset, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    allCategoryModelList.add(new CategoryModel(jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getString("image")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            categoryAdapter.notifyDataSetChanged();
        }, error -> {

        });
        queue.add(jsonArrayRequest);
    }

    private boolean validateEditText(EditText txt, String description) {

        if (txt.getText().toString().trim().length() == 0) {
            Toast.makeText( description, Toast.LENGTH_SHORT).show();
            txt.requestFocus();
            return true;
        }
        return false;
    }
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnresetpass:
                if (!(validateEditText(, "Tài khoản không được rỗng.")
                        || validateEditText(password_login, "Mật khẩu không được rỗng."))) {
                    Login();
                }
                break;
            case R.id.btnReset:
                startActivity(new Intent(context, ResetPassActivity.class));
                break;

        }
    }

}