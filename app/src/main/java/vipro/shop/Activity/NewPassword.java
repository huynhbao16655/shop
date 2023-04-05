package vipro.shop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import vipro.shop.Model.Server;
import vipro.shop.Model.Support;
import vipro.shop.R;

public class NewPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        String email = getIntent().getExtras().getString("email");
        EditText editTextNewPassword = findViewById(R.id.new_password);
        EditText editTextOTP = findViewById(R.id.otp);
        Button button = findViewById(R.id.btnConfirm);
        ProgressBar progressBar = findViewById(R.id.progress);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                progressBar.setVisibility(view.VISIBLE);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlNewPass,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(view.GONE);
                                if(response.equals("success")){
                                    Toast.makeText(getApplicationContext(),"New Password hasbeen set",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
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
                        paramV.put("email", email);
                        paramV.put("otp", editTextOTP.getText().toString());
                        paramV.put("new-password", Support.EndcodeMD5(editTextNewPassword.getText().toString()));
                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });
    }

}