package vipro.shop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import vipro.shop.R;

public class ResetPassActivity extends AppCompatActivity {
    EditText email;
    AppCompatButton btnreset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        initControl();
    }
    private void initControl(){
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email = email.getText().toString().trim();
                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(ResetPassActivity.this, "Chưa Nhập Email", Toast.LENGTH_SHORT).show();
                }else {

                }
            }
        });
    }
    private void  initView() {
        email = findViewById(R.id.edtresetpass);
        btnreset = findViewById(R.id.bntresetpass);
    }
}