package com.acra.tlm.common;


import com.acra.tlm.model.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiClient {
   /* @POST(AppConstant.LOGIN_URL)
    Call<String> getLoginResponse(  @Header("Username") String username,
                                    @Header("Password") String password);*/
    @POST("manage/mgmtrestservice/authenticate")
    Call<LoginResponse> basicLogin();
}
