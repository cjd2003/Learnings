package com.acra.tlm.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.acra.tlm.common.ApiClient;
import com.acra.tlm.common.ManageSharedPreferences;
import com.acra.tlm.model.LoginRequest;
import com.acra.tlm.model.LoginResponse;
import com.acra.tlm.util.BaseService;
import com.acra.tlm.view.activity.LoginActivity;
import com.acra.tlm.view.activity.TargetActivity;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginService extends BaseService {
    private OnLoginListener mCallback;
    private Context mContext;
    private Call<String> mCall;
    Boolean isError = false;


    public LoginService(OnLoginListener mCallback, Context mContext) {
        this.mCallback = mCallback;
        this.mContext = mContext;

    }

    @Override
    public void cancelRequest() {
        mCall = null;
        mCallback = null;

    }


    public void getResponse(String username, String password) {

        ApiClient apiClient =
                //ServiceGenerator.createService(ApiClient.class, "jde_admin", "Qsoft_2016");
                ServiceGenerator.createService(ApiClient.class,username,password);
//        String credentials = "jde_admin" + ":" + "Qsoft_2016";
//        final String basic =
//                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<LoginResponse> call = apiClient.basicLogin();
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && mCallback != null) {
                    Log.d("res", "" + response.body());
                    Log.d("res", "" + response.headers().get("Set-Cookie"));
                    Log.d("res", "" + response.headers());
                    ManageSharedPreferences.newInstance().saveString(mContext, response.headers().get("Set-Cookie"), ManageSharedPreferences.TOKEN);

                    mCallback.onLoginSuccess();

                    // user object available
                } else {
                    // error response, no access to resource?
                    Log.d("res", "" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //   Toast.makeText(LoginActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                if (mCallback != null) {
                    mCallback.onLoginFailure(t);
                }
            }
        });

    }

    public interface OnLoginListener {
        void onLoginSuccess();
        void onLoginFailure(Throwable error);
    }
}
