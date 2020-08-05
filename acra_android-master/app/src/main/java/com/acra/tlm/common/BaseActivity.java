package com.acra.tlm.common;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;



public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Add this method in BaseActivity out side of onCreate method
    public void replace(int containerId, Fragment fragment, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(containerId, fragment, Integer.toString(getSupportFragmentManager().getBackStackEntryCount()));
        ft.commit();
        fm.executePendingTransactions();
        Log.d("check", "replace");


    }


}
