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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vipro.shop.Model.CategoryModel;
import vipro.shop.Model.Server;
import vipro.shop.R;

public class ResetPassActivity extends AppCompatActivity {
    ImageView backLogin;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        EditText editText = findViewById(R.id.editEmail);
        Button button = findViewById(R.id.btnresetpass);
        backLogin = findViewById(R.id.btn_back_to_login);
        ProgressBar progressBar = findViewById(R.id.progress);
        setClick();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                progressBar.setVisibility(view.VISIBLE);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlReset,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(view.GONE);
                                if(response.equals("success")){
                                    Intent intent = new Intent(getApplicationContext(), NewPassword.class);
                                    intent.putExtra("email", editText.getText().toString());
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(view.GONE);
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", editText.getText().toString());
                        return paramV;
                    }
                };
                stringRequest.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 30000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 30000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {

                    }
                });
                queue.add(stringRequest);

            }
        });
    }

    private void setClick() {
        backLogin.setOnClickListener(view -> {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        });
    }


}