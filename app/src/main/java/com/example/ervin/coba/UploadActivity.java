package com.example.ervin.coba;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static com.example.ervin.coba.R.id.image;
import static com.example.ervin.coba.R.id.namaGambar;

/**
 * Created by ervin on 5/24/2017.
 */

public class UploadActivity extends AppCompatActivity {
    private static final String TAG = "UploadActivity";
    Button btnUpload, btnNext, btnBefore;
    ImageView ivGambar;
    EditText etNamaGambar;
    ProgressDialog progressDialog;
    int panjangGambar = 512, lebarGambar= 512;
    ArrayList<String> lokasiGamabar;
    int posisiGmabar;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halaman_upload);
        btnBefore = (Button) findViewById(R.id.back);
        btnNext = (Button) findViewById(R.id.next);
        btnUpload = (Button) findViewById(R.id.upload);
        ivGambar = (ImageView) findViewById(R.id.gambar);
        etNamaGambar = (EditText) findViewById(R.id.namaGambar);
        lokasiGamabar = new ArrayList<>();
        progressDialog = new ProgressDialog(UploadActivity.this);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        checkFilePermissions();
        addFilePaths();



        btnBefore.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                if(posisiGmabar > 0){

                    Log.d(TAG, "onClick: Back an Image.");

                    posisiGmabar = posisiGmabar - 1;

                    loadImageFromStorage();

                }

            }

        });

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                if(posisiGmabar < lokasiGamabar.size() - 1){

                    Log.d(TAG, "onClick: Next Image.");

                    posisiGmabar = posisiGmabar + 1;

                    loadImageFromStorage();

                }

            }

        });

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Log.d(TAG, "onClick: Uploading Image.");

                progressDialog.setMessage("Uploading Image...");

                progressDialog.show();



                //get the signed in user

                FirebaseUser user = mAuth.getCurrentUser();

                String userID = user.getUid();



                String name = etNamaGambar.getText().toString();

                if(!name.equals("")){

                    Uri uri = Uri.fromFile(new File(lokasiGamabar.get(posisiGmabar)));

                    StorageReference storageReference = mStorageRef.child("images/users/" + userID + "/" + name + ".jpg");

                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override

                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Get a URL to the uploaded content

                        //    @SuppressWarnings("VisibleForTests")  Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            Toast.makeText(UploadActivity.this,"berhasil", Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();

                        }

                    }).addOnFailureListener(new OnFailureListener() {

                        @Override

                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(UploadActivity.this,"gagal", Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();

                        }

                    })

                    ;

                }



            }

        });
    }

    private void addFilePaths(){

        Log.d(TAG, "addFilePaths: Adding file paths.");

        String path = System.getenv("EXTERNAL_STORAGE");

        lokasiGamabar.add(path+"/Pictures/Instagram/aku.jpg");

        lokasiGamabar.add(path+"/Pictures/Instagram/mereka.jpg");

        lokasiGamabar.add(path+"/Pictures/Instagram/saya.jpg");

        loadImageFromStorage();

    }

    private void loadImageFromStorage() {
        try{

            String path = lokasiGamabar.get(posisiGmabar);

            File f=new File(path, "");

            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            ivGambar.setImageBitmap(b);

        }catch (FileNotFoundException e){

            Log.e(TAG, "loadImageFromStorage: FileNotFoundException: " + e.getMessage() );

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkFilePermissions() {

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){

            int permissionCheck = UploadActivity.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");

            permissionCheck += UploadActivity.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");

            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number

            }

        }else{

            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");

        }

    }
}
