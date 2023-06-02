package com.am.aucklank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText tv_name;
    private EditText tv_pwd;

    private static final String SP_INFO = "myuser";
    private static final String USER_ID = "UserId";
    private static final String USERPWD = "UserPwd";
    private TestTableSqlite testTableSqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();

        tv_name = findViewById(R.id.tv_name);
        tv_pwd = findViewById(R.id.tv_pwd);
        Button button = findViewById(R.id.tv_login);
        button.setOnClickListener(this);
        TextView tv_register = findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);


        // record the log of saving data (记录保存数据情况)
        checkIfRemember();
        // create database (创建数据库)

        testTableSqlite = new TestTableSqlite(MainActivity.this);



        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        if(sp.contains("username")){
            startActivity(new Intent(this,MapActivity.class));
            this.finish();
        }
    }

    //save data (存数据)
    public void rememberMe(String uid, String pwd) {
        SharedPreferences sp = getSharedPreferences(SP_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_ID, uid);
        editor.putString(USERPWD, pwd);
        editor.commit();
    }

    //read data (读数据)
    public void checkIfRemember() {
        SharedPreferences sp = getSharedPreferences(SP_INFO, MODE_PRIVATE);
        String uidStr = sp.getString(USER_ID, null);
        String pwdStr = sp.getString(USERPWD, null);
        if (uidStr != null && pwdStr != null) {
            tv_name.setText(uidStr);
            tv_pwd.setText(pwdStr);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        String uidStr = tv_name.getText().toString().trim();
        String pwdStr = tv_pwd.getText().toString().trim();

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        switch (v.getId()) {
            case R.id.tv_login:

                intent.putExtra("flag", 0);
                startActivity(intent);
                break;
            case R.id.tv_register:

                intent.putExtra("flag", 1);
                startActivity(intent);
                break;
        }

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // determining if user has the authorization (先判断有没有权限)
            if (Environment.isExternalStorageManager()) {
                //automatic get authorization (自动获取权限)
                autoObtainLocationPermission();
            } else {
                //jump to setting page (跳转到设置界面引导用户打开)
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else {
            //automatic get authorization (自动获取权限)
            autoObtainLocationPermission();
        }
    }

    private static final int REQUEST_CODE = 100;
    private static final int PERMISSION_STATUS = 1000;

    /**
     * automatic get authorization (自动获取权限)
     */
    private void autoObtainLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.CALL_PHONE
                    },
                    PERMISSION_STATUS);

        } else {
            boolean permissionFlag = true;
        }
    }
}