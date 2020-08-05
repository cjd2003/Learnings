package com.acra.tlm.common;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acra.tlm.R;

public class CommonTask {
    public static void toast(Context context, String message) {
        final View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null); // Code for inflating xml layout
        LinearLayout custom_toast_container = view.findViewById(R.id.custom_toast_container);
        TextView tv = view.findViewById(R.id.tv_error);
        tv.setText(message);
        final Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        //   view.animate().translationY(500);
        toast.show();
        custom_toast_container.animate()
                .translationYBy(120)
                .translationY(0)
                .setDuration(context.getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }
}
