package com.truedevelopment.autolight;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class theViewholder extends RecyclerView.ViewHolder {
    public TextView devicename,deviceid;
    public Button On, Off;
    public Switch aswitch;
    ImageView popUpMenu;
    public theViewholder(@NonNull View itemView) {
        super(itemView);
        devicename =itemView.findViewById(R.id.tvDeviceName);
        deviceid = itemView.findViewById(R.id.tvProductID);
        On = itemView.findViewById(R.id.btnON);
        Off = itemView.findViewById(R.id.btnOFF);
        popUpMenu = itemView.findViewById(R.id.menuMore);
        aswitch = itemView.findViewById(R.id.power_switch);
    }
}
