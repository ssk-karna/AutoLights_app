package com.truedevelopment.autolight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {

    Button userProfile;
    String user_name,user_username,user_email,user_phone,user_password,user_uid;
    FloatingActionButton floatingAddButton, floatingProfileButton;
    TextView emptyMessage;

   private FirebaseRecyclerOptions<Product> options;
   private FirebaseRecyclerAdapter<Product, theViewholder> adapter;
   private RecyclerView recyclerView;
   DatabaseReference ref;
    int maxDeviceCount=0;
   int maxUserCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        floatingAddButton = findViewById(R.id.floatingAddButton);
        floatingProfileButton = findViewById(R.id.floatingProfileButton);
        emptyMessage = findViewById(R.id.emptyMessage);
        recyclerView= findViewById(R.id.recview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy>0)
                {
                    floatingAddButton.hide();
                    floatingProfileButton.hide();
                }else{
                    floatingAddButton.show();
                    floatingProfileButton.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs",MODE_PRIVATE);
         user_name = sp.getString("name","");
        user_username = sp.getString("username","");
        user_email = sp.getString("email","");
        user_phone = sp.getString("phone","");
        user_password = sp.getString("password","");
        user_uid = sp.getString("uid","");
        checkDevicesExist(user_uid);
        ref = FirebaseDatabase.getInstance().getReference("Users").child(user_uid).child("ProductsOwned");

        floatingProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UserIntent = new Intent(getApplicationContext(),UserProfile.class);

               UserIntent.putExtra("name",user_name);
               UserIntent.putExtra("username",user_username);
               UserIntent.putExtra("email",user_email);
               UserIntent.putExtra("phone",user_phone);
               UserIntent.putExtra("password",user_password);
                startActivity(UserIntent);
            }
        });

        floatingAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deviceIntent = new Intent(getApplicationContext(),AddDevice.class);
                deviceIntent.putExtra("email",user_email);
                deviceIntent.putExtra("username",user_username);
                deviceIntent.putExtra("uid",user_uid);

                startActivity(deviceIntent);
            }
        });

         options = new FirebaseRecyclerOptions.Builder<Product>().setQuery(ref,Product.class).build();
         adapter = new FirebaseRecyclerAdapter<Product, theViewholder>(options) {
             @Override
             protected void onBindViewHolder(@NonNull theViewholder holder, int position, @NonNull Product model) {

                 final String lastControlledBy = user_email;
                 final String name_username = user_username;

                 holder.devicename.setText(""+model.getnickname());
                 holder.deviceid.setText(""+model.getProductID());

                 final String productid = holder.deviceid.getText().toString();
                 final String productname = holder.devicename.getText().toString();
                 final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("devices").child(productid);
                 holder.On.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         ref.child("LED_STATUS").setValue("ON");
                         ref.child("lastControlledBy").setValue(lastControlledBy);
                     }
                 });
                 holder.Off.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         ref.child("LED_STATUS").setValue("OFF");
                         ref.child("lastControlledBy").setValue(lastControlledBy);
                     }
                 });

                 holder.popUpMenu.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                         popupMenu.getMenuInflater().inflate(R.menu.pop_menu,popupMenu.getMenu());
                         popupMenu.show();
                         popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                             @Override
                             public boolean onMenuItemClick(MenuItem item) {
                                 switch (item.getItemId()){
                                     case R.id.menu_edit: {
                                         Intent editActivityIntent = new Intent(getApplicationContext(), edit_menu.class);

                                         editActivityIntent.putExtra("editname", productname);
                                         editActivityIntent.putExtra("productid", productid);
                                         editActivityIntent.putExtra("username", name_username);

                                         startActivity(editActivityIntent);
                                         break;
                                     }
                                         case R.id.menu_delete:
                                         {  DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(name_username).child("ProductsOwned");
                                         Query checkDeviceList = productsRef.orderByChild("nickname");
                                         checkDeviceList.addValueEventListener(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                 if (snapshot.exists()){
                                                     maxDeviceCount = (int) snapshot.getChildrenCount();
                                                 }
                                             }

                                             @Override
                                             public void onCancelled(@NonNull DatabaseError error) {

                                             }
                                         });
                                         DatabaseReference userRef = ref.child("users");
                                         Query checkUserList = userRef;
                                         checkUserList.addValueEventListener(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                 if (snapshot.exists()){
                                                     maxUserCount = (int) snapshot.getChildrenCount();
                                                 }
                                             }

                                             @Override
                                             public void onCancelled(@NonNull DatabaseError error) {

                                             }
                                         });

                                         if(maxDeviceCount!=1) {
                                             productsRef.child(productid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {

                                                 }
                                             });
                                         }
                                             else{
                                                 productsRef.setValue("null").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {

                                                     }
                                                 });
                                             }
                                             if(maxUserCount!=1) {
                                                 userRef.child(user_username).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {

                                                     }
                                                 });
                                             }
                                             else{
                                                 userRef.setValue("null").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {

                                                     }
                                                 });
                                             }
                                             break;
                                     }
                                 }
                                 return true;
                             }
                         });
                     }
                 });
             }

             @NonNull
             @Override
             public theViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                 View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
                 return new theViewholder(v);
             }
         };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void checkDevicesExist(String username){

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Users").child(username).child("ProductsOwned");
        Query checkDevice = dbref.orderByChild("username").equalTo("null");
        checkDevice.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    emptyMessage.setVisibility(View.VISIBLE);
                    //emptyMessage.setVisibility(View.VISIBLE);
                    //recyclerView.setVisibility(View.GONE);
                }
                else{
                    //recyclerView.setVisibility(View.VISIBLE);
                    emptyMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}