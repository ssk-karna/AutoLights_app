package com.truedevelopment.autolight;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class theViewholder extends RecyclerView.ViewHolder {
    public TextView devicename,deviceid;
    public theViewholder(@NonNull View itemView) {
        super(itemView);
        devicename =itemView.findViewById(R.id.tvDeviceName);
        deviceid = itemView.findViewById(R.id.tvProductID);
    }
}
