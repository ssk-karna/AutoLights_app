package com.truedevelopment.autolight;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MyAdapter extends FirebaseRecyclerAdapter<Product,MyAdapter.MyViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull Product product) {

        myViewHolder.devicename.setText(product.getDeviceName());
        myViewHolder.deviceid.setText(product.getProductID());

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView devicename,deviceid;
        Button on,off;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            devicename = (TextView)itemView.findViewById(R.id.tvDeviceName);
            deviceid = (TextView)itemView.findViewById(R.id.tvProductID);
            on = (Button)itemView.findViewById(R.id.btnON);
            off = (Button)itemView.findViewById(R.id.btnOFF);
        }
    }
}
