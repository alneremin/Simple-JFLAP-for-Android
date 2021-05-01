package com.sfu_kras.stud.aeremin_ki18.sjflap;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.login.ui.login.LoginActivity;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.register.ui.register.RegisterActivity;

public class StartActivity extends AppCompatActivity {

    //final TextView startTitle = findViewById(R.id.start_name);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);


        ((TextView)findViewById(R.id.start_name))
                .setShadowLayer(3, 0, 0, Color.BLACK);
        Button btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Button btn_register = (Button) findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


}