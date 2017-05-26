package com.example.ervin.coba;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ervin on 5/24/2017.
 */

public class EditProfile extends AppCompatActivity {
    Button btnEdit;
    EditText ETemail, ETnama, ETpekerjaan;
    private FirebaseAuth mAuth;
    TextView a;
    String userID;
    private static final String TAG = "EditProfile";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_layout);
        btnEdit = (Button) findViewById(R.id.edit);
        ETemail = (EditText) findViewById(R.id.EPemail);
        ETnama = (EditText) findViewById(R.id.EPnama);
        ETpekerjaan = (EditText) findViewById(R.id.EPpekerjaan);
        a= (TextView) findViewById(R.id.dd);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("user");
        final DatabaseReference myRef2 = database.getReference().child("admin");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    ProfilPengguna person = childSnapshot.getValue(ProfilPengguna.class);
                   // String value = dataSnapshot.getValue(String.class);
                    String string = "Name: "+person.getAnak();
                    a.setText(string);
                    String aa = dataSnapshot.getKey();
                    Log.d(TAG, "Value is: " + aa);
                }
                }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ETemail.getText().toString();
                String nama = ETnama.getText().toString();
                String pekerjaan = ETpekerjaan.getText().toString();
                if(email.isEmpty()||nama.isEmpty()||pekerjaan.isEmpty()){
                    Toast.makeText(EditProfile.this,"anda isi dulu", Toast.LENGTH_SHORT).show();
                } else {
                    ProfilPengguna profil=new ProfilPengguna(email,nama,pekerjaan);
                    myRef.child(userID).setValue(profil);
                    myRef2.child(userID).setValue(profil);
                }
            }
        });
    }
}
