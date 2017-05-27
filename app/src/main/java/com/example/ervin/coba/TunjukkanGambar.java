package com.example.ervin.coba;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ervin on 5/27/2017.
 */

public class TunjukkanGambar extends AppCompatActivity {
    // Write a message to the database
    private static final String TAG = "TunjukkanGambar";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    RecyclerView rv;
    FirebaseRecyclerAdapter<ItemGambar, TunjukanDataHolder> mFirebaseAdapter;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    public TunjukkanGambar() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tunjukan_data_layout);
        mAuth = FirebaseAuth.getInstance();
        rv = (RecyclerView) findViewById(R.id.recycleViewGamabar);
        rv.setLayoutManager(new LinearLayoutManager(TunjukkanGambar.this));
        progressDialog = new ProgressDialog(TunjukkanGambar.this);
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        myRef = database.getReference().child("gambar").child(userID);

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.setMessage("Uploading Image...");
        progressDialog.show();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ItemGambar, TunjukanDataHolder>(ItemGambar.class, R.layout.data_item_layout, TunjukanDataHolder.class,myRef) {
            @Override
            protected void populateViewHolder(TunjukanDataHolder viewHolder, ItemGambar model, final int position) {
                viewHolder.judul_gambar(model.getNama_gambar());
                viewHolder.link_gambar(model.getLink_gambar());
                String s= model.getNama_gambar();


                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int gambarDipilih =position;
                        String abu = mFirebaseAdapter.getRef(gambarDipilih).getKey();//untuk mengambil key path dari item yng diliih
                        Log.d(TAG, "lokasi path : "+abu );
                    }
                });
            }

        };

        rv.setAdapter(mFirebaseAdapter);
        progressDialog.dismiss();
    }
}
