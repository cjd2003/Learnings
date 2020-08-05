package com.acra.tlm.model.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.acra.tlm.R;
import com.acra.tlm.model.TargetModel;
import com.acra.tlm.view.activity.LoginActivity;

import java.util.List;

public class LoginSpinnerAdapter extends ArrayAdapter<TargetModel> {
    private Context context;
    // Your custom values for the spinner (User)
    // private Mappingelements[] values;
    private List<TargetModel> values;


    public LoginSpinnerAdapter(Context context, int resource, List<TargetModel> objects, LoginActivity loginActivity) {
        super(context, resource, objects);
        this.values =objects;
        this.context = context;
    }


    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public TargetModel getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setText(values.get(position).getTargetName());
        label.setTextColor(Color.BLACK);
        return label;
    }







    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getTargetName());
        label.setPadding(10,15,5,0);
        label.setGravity(Gravity.CENTER);
      //  label.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rectangle));

        return label;
    }



}
