package com.acra.tlm.view.activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.acra.tlm.R;
import com.acra.tlm.common.ApiClient;
import com.acra.tlm.common.BaseActivity;
import com.acra.tlm.common.CommonTask;
import com.acra.tlm.common.ManageSharedPreferences;
import com.acra.tlm.model.LoginResponse;
import com.acra.tlm.model.TargetModel;
import com.acra.tlm.model.adapter.LoginSpinnerAdapter;
import com.acra.tlm.service.LoginService;
import com.acra.tlm.service.ServiceGenerator;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.annotation.Target;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends BaseActivity {
    Context mContext;

    // @BindView(R.id.et_user_name)
    public static EditText etUserName;

    @BindView(R.id.btn_login)
    Button btn_login;

    // @BindView(R.id.et_password)
    public static EditText etPassword;

    @BindView(R.id.sp_target_name)
    Spinner spTargetName;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.saveLoginCheckBox)
    CheckBox saveLoginCheckBox;


    String name = null;
    private ManageSharedPreferences manageSharedPreferences;
    private SpinnerAdapter spinnerAdapter;

    LoginService loginService;
    List<TargetModel> list;
    String tgtNameSpin;
    SharedPreferences prefs = null;
//    boolean firstTime;
    String firstTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;
        ButterKnife.bind(this);
        manageSharedPreferences = ManageSharedPreferences.newInstance();
        etUserName = findViewById(R.id.et_user_name);
        etPassword = findViewById(R.id.et_password);
//        Toast.makeText(mContext, Boolean.toString(firstTime), Toast.LENGTH_SHORT).show();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);           // checking if the app is run for the first time
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            firstTime = "true";
            SharedPreferences prefs1 = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs1.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        } else {
            firstTime = "false";
        }
        Toast.makeText(mContext, (firstTime), Toast.LENGTH_LONG).show();




        Intent i = getIntent();
        tgtNameSpin = i.getStringExtra("spinnerFirst");       // use this to set as the first in spinner

        if (manageSharedPreferences.getBoolean(mContext, ManageSharedPreferences.CHECKBOX)) {
            etPassword.setText(manageSharedPreferences.getString(mContext, ManageSharedPreferences.USER_PASSWORD));
            etUserName.setText(manageSharedPreferences.getString(mContext, ManageSharedPreferences.USER_NAME));
            saveLoginCheckBox.setChecked(true);
            etUserName.setCursorVisible(false);
        }

        addItemsOnSpinner();
        checkSpinnerData();


    }



    @OnClick(R.id.img_gear)
    public void onClickImgGear(View view) {
        int position = spTargetName.getSelectedItemPosition();
        Intent userToTarget = new Intent(mContext, TargetActivity.class);
        userToTarget.putExtra("firstTimeRun", firstTime);
        if (position == -1 ) {     // if spinner is empty
            startActivity(userToTarget);
        }  else {              // if spinner not empty
            TargetModel targetName = (TargetModel) spTargetName.getItemAtPosition(position);
            userToTarget.putExtra("Target Name", targetName.getTargetName());
        }
        startActivity(userToTarget);


    }

    @OnClick(R.id.btn_login)
    public void onClickLogin(View view) {
        progressBar.setVisibility(View.VISIBLE);
        if (etUserName.getText().toString().trim().isEmpty()) {
            //etUserName.setError("User name field can't be empty");
            CommonTask.toast(mContext,"Please enter user name");
            progressBar.setVisibility(View.GONE);
        } else if (etPassword.getText().toString().trim().isEmpty()) {
            //etPassword.setError("Password field can't be empty");
            CommonTask.toast(mContext,"Please enter Password");
            progressBar.setVisibility(View.GONE);
        } else {
            callLoginApi();

        }
    }


    //HIDE SOFT KEYBOARD
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void addItemsOnSpinner() {
        list = new ArrayList<TargetModel>();
        list = manageSharedPreferences.getArrayList(ManageSharedPreferences.TARGET_NAME, mContext);
//        String isShrdPrefEmpty = ManageSharedPreferences.newInstance().getArrayList(ManageSharedPreferences.TARGET_NAME,mContext).get(0).getTargetName();
//        Toast.makeText(mContext, isShrdPrefEmpty, Toast.LENGTH_LONG).show();


            if (list != null) {
                spinnerAdapter = new LoginSpinnerAdapter(mContext,
                        android.R.layout.simple_spinner_item,
                        list, LoginActivity.this) {
                };
                spTargetName.setAdapter(spinnerAdapter);
            }
//            if (firstTime.equals("false")){
                if (indexOfTgtName(tgtNameSpin) > -1) {                      // this is to get the target name from the target activity and display it in the spinner
                    spTargetName.setSelection(indexOfTgtName(tgtNameSpin)); // need an int inside
                }
//            }

    }



    public void checkSpinnerData() {
        if (spTargetName != null && spTargetName.getSelectedItem() != null) {
            // name = (String)spTargetName.getSelectedItem();
            btn_login.setVisibility(View.VISIBLE);

        } else {
            btn_login.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addItemsOnSpinner();
        checkSpinnerData();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void callLoginApi() {
        ApiClient apiClient =
                ServiceGenerator.createService(ApiClient.class, etUserName.getText().toString().trim(), etPassword.getText().toString().trim());

        Call<LoginResponse> call = apiClient.basicLogin();
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Log.d("res", "" + response.body());
                    Log.d("res", "" + response.headers().get("Set-Cookie"));
                    Log.d("res", "" + response.headers());
                    ManageSharedPreferences.newInstance().saveString(mContext, response.headers().get("Set-Cookie"), ManageSharedPreferences.TOKEN);


                    if (saveLoginCheckBox.isChecked()) {
                        ManageSharedPreferences.newInstance().saveBoolean(mContext, true, ManageSharedPreferences.CHECKBOX);
                        ManageSharedPreferences.newInstance().saveString(mContext, etUserName.getText().toString().trim(), ManageSharedPreferences.USER_NAME);
                        ManageSharedPreferences.newInstance().saveString(mContext, etPassword.getText().toString().trim(), ManageSharedPreferences.USER_PASSWORD);
                    } else {

                        mContext.getSharedPreferences(ManageSharedPreferences.USER_NAME, 0).edit().clear().apply();
                        mContext.getSharedPreferences(ManageSharedPreferences.CHECKBOX, 0).edit().clear().apply();
                        mContext.getSharedPreferences(ManageSharedPreferences.USER_PASSWORD, 0).edit().clear().apply();
                    }


                    // user object available
                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.d("res", "" + response.errorBody());
                    CommonTask.toast(mContext,"Invalid Credentials");
                   // Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                CommonTask.toast(mContext,"something went wrong");
              //  Toast.makeText(LoginActivity.this, "failure" + " " + "something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public int indexOfTgtName (String tgtName){
        int index = -1;
//        if (firstTime.equals("false")) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTargetName().equals(tgtName)) {
                    index = i;
                }
//            }
        }
        return index;

    }


}
