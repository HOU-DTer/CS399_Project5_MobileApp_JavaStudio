package com.am.aucklank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.ex.DbException;

import java.util.Random;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {


    private ImageButton ibBack;

    private ImageView ivClear1;
    private ImageView ivClear2;

    private TextView tvHeader;

    private TextView tvTitle;
    private TextView tvDesc;
    private TextView tvObtain;
    private TextView tvForgot;


    private EditText et1;
    private EditText et2;

    private Button btnSubmit;


    private LinearLayout llBottom;

    private CheckBox cbAgree;


    private int flag = 0;

    //0 login
    //1 sign up
    //2 forgot password
    //3 reset password


    private int code = 0;

    private UserBean user = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);





        ibBack = findViewById(R.id.login_back);
        ivClear1 = findViewById(R.id.login_clear1);
        ivClear2 = findViewById(R.id.login_clear2);
        tvHeader = findViewById(R.id.login_header);
        tvTitle = findViewById(R.id.login_title);
        tvDesc = findViewById(R.id.login_desc);
        tvObtain = findViewById(R.id.login_obtain_code);
        tvForgot = findViewById(R.id.login_forgot);
        et1 = findViewById(R.id.login_et1);
        et2 = findViewById(R.id.login_et2);

        btnSubmit = findViewById(R.id.login_submit);
        llBottom = findViewById(R.id.login_bottom);

        cbAgree = findViewById(R.id.login_checkbox);


        et1.addTextChangedListener(this);
        et2.addTextChangedListener(this);


        ibBack.setOnClickListener(this);
        ivClear1.setOnClickListener(this);
        ivClear2.setOnClickListener(this);

        tvHeader.setOnClickListener(this);
        tvForgot.setOnClickListener(this);

        tvObtain.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);


        Bundle args = getIntent().getExtras();
        if (args != null) {
            this.setFlag(args.getInt("flag"));
        }

        if (this.flag == 1) {
            showTip();
        }

    }

    private void showTip() {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage("To help other users provide more accurate information and improve " +
                        "the reliability of the software, please fill the REPORT")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

    }


    private void setFlag(int flag) {
        this.flag = flag;

        et1.setText("");
        et2.setText("");
        if (this.flag == 0) {
            this.tvHeader.setText("Code login");
            this.tvTitle.setText("Password login");
            this.tvDesc.setText("Please enter your account and password correctly");
            ivClear1.setVisibility(View.GONE);


            et1.setHint("Account number");
            et2.setHint("Password");

            tvObtain.setVisibility(View.GONE);
            ivClear2.setVisibility(View.GONE);

            btnSubmit.setText("Sign in");
            tvForgot.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.VISIBLE);


            et1.setInputType(InputType.TYPE_CLASS_NUMBER);
            et1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            et2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});

        } else if (this.flag == 1) {
            this.tvHeader.setText("Password login");
            this.tvTitle.setText("Verification code login");
            this.tvDesc.setText("If the phone number is not registered, we will automatically register it for you");


            et2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            et2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

            et1.setHint("Phone number");
            et2.setHint("Verification Code");

            ivClear1.setVisibility(View.GONE);

            tvObtain.setVisibility(View.GONE);
            ivClear2.setVisibility(View.GONE);

            btnSubmit.setText("Sign in");
            tvForgot.setVisibility(View.VISIBLE);

            llBottom.setVisibility(View.VISIBLE);

            tvForgot.setVisibility(View.GONE);
        } else if (this.flag == 2) {
            this.tvHeader.setText("");
            this.tvTitle.setText("Forgot account password");
            this.tvDesc.setText("Verify phone number and reset password");

            et1.setHint("Phone number");
            et2.setHint("Verification Code");

            ivClear1.setVisibility(View.GONE);

            tvObtain.setVisibility(View.GONE);
            ivClear2.setVisibility(View.GONE);

            btnSubmit.setText("Sign in");
            tvForgot.setVisibility(View.VISIBLE);

            llBottom.setVisibility(View.VISIBLE);

            tvForgot.setVisibility(View.GONE);
            btnSubmit.setText("Submit");
            tvForgot.setVisibility(View.GONE);
            llBottom.setVisibility(View.GONE);


            et2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            et2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        } else if (flag == 3) {
            tvHeader.setVisibility(View.GONE);
            tvTitle.setText("Reset Password");
            tvDesc.setText("A password must be at least eight characters long");
            et1.setHint("New password");
            et2.setHint("Retype new password");

            et1.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            et1.setTransformationMethod(PasswordTransformationMethod.getInstance());

            et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            et2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
            et2.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);

            tvObtain.setVisibility(View.GONE);

            btnSubmit.setText("Submit");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_back:
                LoginActivity.this.finish();
                break;
            case R.id.login_clear1:
                et1.setText("");
                break;
            case R.id.login_clear2:
                et2.setText("");
                break;

            case R.id.login_forgot:
                setFlag(2);
                break;

            case R.id.login_obtain_code:
                this.code = randomCode(100000, 999999);
                Toast.makeText(LoginActivity.this, "The verification code: " + this.code + ", has been sent to" + et1.getText() + ", please check it!", Toast.LENGTH_LONG).show();
                break;
            case R.id.login_submit:
                String username = et1.getText().toString().replace(" ", "");
                UserBean user = null;
                try {
                    user = MyApplication.db.selector(UserBean.class).where("username", "=", username).findFirst();

                } catch (DbException e) {
                    throw new RuntimeException(e);
                }
                if (this.flag == 0) {

                    if (!this.cbAgree.isChecked()) {
                        Toast.makeText(this, "Please tick the agreement", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (user == null) {
                        Toast.makeText(this, "Username does not exist", Toast.LENGTH_LONG).show();
                    } else {
                        String password = et2.getText().toString();

                        if (user.getPassword().equals(password)) {
                            Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show();
                            gotoMap();
                        } else {
                            Toast.makeText(this, "Password error!", Toast.LENGTH_LONG).show();
                        }
                    }

                } else if (this.flag == 1) {

                    if (!this.cbAgree.isChecked()) {
                        Toast.makeText(this, "Please tick the agreement", Toast.LENGTH_LONG).show();
                        return;
                    }

                    int code = Integer.valueOf(et2.getText().toString());
                    if (code != this.code) {
                        Toast.makeText(this, "Verification code error!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (user == null) {
                        user = new UserBean();
                        user.setUsername(username);
                        user.setPassword("12345678");
                        try {
                            MyApplication.db.save(user);
                            Toast.makeText(this, "Successful registration for new user, initial password: 12345678", Toast.LENGTH_LONG).show();
                            this.setFlag(0);
                        } catch (DbException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show();
                        gotoMap();
                    }

                } else if (this.flag == 2) {
                    int code = Integer.valueOf(et2.getText().toString());
                    if (code != this.code) {
                        Toast.makeText(this, "Verification code error!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(this, "Verification successful", Toast.LENGTH_LONG).show();


                    if (user == null) {
                        this.user = new UserBean();
                        this.user.setUsername(et1.getText().toString());
                        this.user.setPassword("12345678");
                        try {
                            MyApplication.db.save(this.user);
                        } catch (DbException e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(this, "Successful registration for new user, initial password: 12345678", Toast.LENGTH_LONG).show();
                    } else {
                        this.user = user;
                    }
                    setFlag(3);
                } else if (this.flag == 3) {
                    try {
                        if (et1.getText().toString().equals(et2.getText().toString())) {
                            this.user.setPassword(et1.getText().toString());
                            MyApplication.db.update(this.user);
                            Toast.makeText(this, "Password changed successful!", Toast.LENGTH_LONG).show();
                            this.setFlag(0);
                        } else {
                            Toast.makeText(this, "Two inconsistencies in password entry!", Toast.LENGTH_LONG).show();
                        }
                    } catch (DbException e) {
                        throw new RuntimeException(e);
                    }

                }
                break;

            case R.id.login_header:
                if (this.flag == 0) {
                    this.setFlag(1);
                } else {
                    this.setFlag(0);
                }
                break;
            default:
                break;
        }
    }


    private void gotoMap() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("username", et1.getText().toString());
        ed.commit();
        finish();
        startActivity(new Intent(this, MapActivity.class));
    }

    private int randomCode(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (this.flag == 1 || this.flag == 2) {
            String text = et1.getText().toString().trim();
            if (text.length() == 10) {
                text = text.substring(0, 3) + " " + text.substring(3, 7) + " " + text.substring(7);
                et1.setText(text);
                et1.setSelection(et1.getText().length());
            } else if (text.length() == 12) {
                tvObtain.setVisibility(View.VISIBLE);
            } else {
                if (text.indexOf(" ") > 0) {
                    et1.setText(text.replaceAll(" ", ""));
                }
                tvObtain.setVisibility(View.GONE);
                et1.setSelection(et1.getText().length());
            }
        }

        if (et1.getText().toString().trim().length() > 0) {
            ivClear1.setVisibility(View.VISIBLE);
        } else {
            ivClear1.setVisibility(View.GONE);
        }


        if (et2.getText().toString().trim().length() > 0) {
            ivClear2.setVisibility(View.VISIBLE);
        } else {
            ivClear2.setVisibility(View.GONE);
        }

        if (this.flag == 1 || this.flag == 2) {
            if (et1.getText().toString().trim().length() >= 6 && et2.getText().toString().trim().length() >= 6) {
                btnSubmit.setEnabled(true);
            } else {
                btnSubmit.setEnabled(false);
            }
        } else if (this.flag == 0 || this.flag == 3) {
            if (et1.getText().toString().trim().length() >= 8 && et2.getText().toString().trim().length() >= 8) {
                btnSubmit.setEnabled(true);
            } else {
                btnSubmit.setEnabled(false);
            }
        }

    }
}