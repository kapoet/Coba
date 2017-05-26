package com.example.ervin.coba;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ervin on 5/23/2017.
 */

public class LihatProfile extends AppCompatActivity{

    private static final String TAG = "LihatProfile";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ListView lvProfile;
    String userID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halaman_profile);
        lvProfile = (ListView) findViewById(R.id.listProfile);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final DatabaseReference usersRef = myRef.child("user");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        Log.d(TAG, "onCreate: "+userID);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
          //     for (DataSnapshot ds: dataSnapshot.getChildren()){
          //          ProfilPengguna pf=new ProfilPengguna();
                    ProfilPengguna pf = dataSnapshot.getValue(ProfilPengguna.class);
                    pf.setEmail(dataSnapshot.child(userID).getValue(ProfilPengguna.class).getEmail());
                    pf.setNama(dataSnapshot.child(userID).getValue(ProfilPengguna.class).getNama());
                    pf.setPekerjaan(dataSnapshot.child(userID).getValue(ProfilPengguna.class).getPekerjaan());
                String aa= dataSnapshot.getKey();
                Log.d(TAG, "showData: name: " + aa);
                    Log.d(TAG, "showData: name: " + pf.getNama());

                    Log.d(TAG, "showData: email: " + pf.getEmail());

                    Log.d(TAG, "showData: phone_num: " + pf.getPekerjaan());
                    ArrayList<String> dataPengguna = new ArrayList<>();
                    dataPengguna.add(pf.getNama());
                    dataPengguna.add(pf.getEmail());
                    dataPengguna.add(pf.getPekerjaan());
                    ArrayAdapter adapter = new ArrayAdapter(LihatProfile.this,android.R.layout.simple_list_item_1,dataPengguna);
                    lvProfile.setAdapter(adapter);//lllll
            //    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
