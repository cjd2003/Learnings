package com.acra.tlm.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acra.tlm.R;
import com.acra.tlm.common.CommonTask;
import com.acra.tlm.common.ManageSharedPreferences;
import com.acra.tlm.model.TargetModel;
import com.acra.tlm.service.ServiceGenerator;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TargetActivity extends AppCompatActivity {

    @BindView(R.id.et_target_name)
    EditText etTargetName;

    @BindView(R.id.et_url)
    EditText etTargetUrl;

    @BindView(R.id.btn_save)
    TextView btnSave;

    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @BindView(R.id.btn_addNewTarget)
    Button btnAddNewTarget;

    ManageSharedPreferences manageSharedPreferences;
    Context mContext;
    String data;
    String firstTime;

    ArrayList<TargetModel> savedData ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        Intent i = getIntent();
        String tgtName = i.getStringExtra("Target Name");   //This receives the the target name that is in the spinner in Login Activity
        firstTime = i.getStringExtra("firstTimeRun");

        mContext = TargetActivity.this; // This initializes the environment we are working on
        ButterKnife.bind(this);
        etTargetName.setText(tgtName);
        etTargetUrl.setText(ServiceGenerator.getApiBaseUrl());  // set server manager url
        manageSharedPreferences = ManageSharedPreferences.newInstance();

        //Listening to the Cancel button click and moving to the LoginActivity
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takeUserLogin = new Intent(mContext, LoginActivity.class);
                startActivity(takeUserLogin);
                finish();
            }
        });


        //Listening to the Add Button and reopening a new Target Page
        btnAddNewTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reopenTarget = new Intent(mContext, TargetActivity.class);
                startActivity(reopenTarget);
            }
        });


    }

    @OnClick(R.id.btn_save)
    public void onSaveClick(View view) {
        data = etTargetName.getText().toString().trim();
        boolean isPresent = false;

//        if(firstTime.equals("false")) {
            for (int i = 0; i < ManageSharedPreferences.newInstance().getArrayList(ManageSharedPreferences.TARGET_NAME, mContext).size(); i++) {
                if (ManageSharedPreferences.newInstance().getArrayList(ManageSharedPreferences.TARGET_NAME, mContext).get(i).getTargetName().equals(data)) {
                    isPresent = true;
                }
//            }
        }


        if(isPresent == false){
            if ((isValidPassword(etTargetName.getText().toString()))) {
                Toast.makeText(this, "Valid", Toast.LENGTH_SHORT).show();
                TargetModel targetModel = new TargetModel();
                targetModel.setTargetName(data);
                targetModel.setTargetUrl(etTargetUrl.getText().toString().trim());
                savedData =  ManageSharedPreferences.newInstance().getArrayList(ManageSharedPreferences.TARGET_NAME,mContext);

                if (savedData != null){

                    savedData =  ManageSharedPreferences.newInstance().getArrayList(ManageSharedPreferences.TARGET_NAME,mContext);
                    savedData.add(targetModel);
                    System.out.print(savedData);
//                String txt = ManageSharedPreferences.newInstance().getString(mContext, ManageSharedPreferences.TARGET_NAME);
//                Toast.makeText(mContext, txt, Toast.LENGTH_LONG).show();                    // Delete this TEST Toast after testing
                    Intent intent = new Intent(mContext, LoginActivity.class);    // New activity
                    intent.putExtra("spinnerFirst", data);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else {
                    savedData = new ArrayList<TargetModel>();
                    savedData.add(targetModel);
//                String txt = ManageSharedPreferences.newInstance().getString(mContext, ManageSharedPreferences.TARGET_NAME);
//                Toast.makeText(mContext, txt, Toast.LENGTH_LONG).show();                     // Delete this TEST Toast after testing
                }
                ManageSharedPreferences.newInstance().saveArrayList(savedData, ManageSharedPreferences.TARGET_NAME, mContext);



            } else {
                // Toast.makeText(this, "Target name is not valid", Toast.LENGTH_SHORT).show();
                //  etTargetName.setError("Target name must contains alpha numeric with special character");
                CommonTask.toast(mContext,"Target name must contain alphanumeric characters");
            }
            if (etTargetName.getText().toString().trim().isEmpty() ){  // this if statement runs ONLY if Target name is EMPTY
                //|| etTargetUrl.getText().toString().trim().isEmpty()) {
                // etTargetUrl.setError("Target name is required");
                //  etTargetUrl.setError("Url is required");
                CommonTask.toast(mContext,"Target name is required");
            } else {
                ManageSharedPreferences.newInstance().saveString(mContext, etTargetUrl.getText().toString().trim(), ManageSharedPreferences.TARGET_URL); // This saves the target url but not the target name because it is empty
            }
        } else {
            CommonTask.toast(mContext,"Target name has already been used!");
        }
//        Toast.makeText(mContext, ManageSharedPreferences.newInstance().getArrayList(ManageSharedPreferences.TARGET_NAME,mContext).get(4).getTargetName(), Toast.LENGTH_LONG).show();

    }

    public static boolean isValidPassword(final String password) {    // This is basically to check if the Target name is right not the Password :)
        Pattern pattern;
        Matcher matcher;
//         final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        final String PASSWORD_PATTERN = "^.*(?=.{4,10})(?=.*\\d)(?=.*[a-zA-Z]).{4,}$"; //
 //
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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


}
