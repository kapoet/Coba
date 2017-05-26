package com.example.ervin.coba;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

import static com.example.ervin.coba.R.id.image;
import static com.example.ervin.coba.R.id.namaGambar;

/**
 * Created by ervin on 5/24/2017.
 */

public class UploadActivity extends AppCompatActivity {
    private static final String TAG = "UploadActivity";
    Button btnUpload, btnImageChooser;
    ImageView ivGambar;
    EditText etNamaGambar;
    ProgressDialog progressDialog;
    ArrayList<String> lokasiGamabar;
    int posisiGmabar;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    String rawPath;
    String path;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halaman_upload);
        btnImageChooser = (Button) findViewById(R.id.imageChooser);
        btnUpload = (Button) findViewById(R.id.upload);
        etNamaGambar = (EditText) findViewById(R.id.namaGambar);
        progressDialog = new ProgressDialog(UploadActivity.this);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        checkFilePermissions();



        btnImageChooser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UploadActivity.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Uploading Image.");
                progressDialog.setMessage("Uploading Image...");
                progressDialog.show();
                FirebaseUser user = mAuth.getCurrentUser();
                final String userID = user.getUid();
                String name = etNamaGambar.getText().toString();

                    Uri uri = Uri.fromFile(new File(path));
                    StorageReference storageReference = mStorageRef.child("images/users/" + userID + "/" + name);

                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override

                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            @SuppressWarnings("VisibleForTests")  Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            String linkDownload = String.valueOf(downloadUrl);
                            myRef.child("gambar").child(userID).child("linkgambar").setValue(linkDownload);
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
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

            for (int i = 0; i < images.size(); i++) {
                Uri uri = Uri.fromFile(new File(images.get(i).path));
                // start play with image uri
                rawPath= String.valueOf(uri);
            }
            path = rawPath.replaceAll("file://","");// untuk menghilangkan "file//" pada path gambar
            String filename=path.substring(path.lastIndexOf("/")+1);//untuk mendapatkan nama file
            etNamaGambar.setText(filename);
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
