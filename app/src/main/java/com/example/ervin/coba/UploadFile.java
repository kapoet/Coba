package com.example.ervin.coba;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

/**
 * Created by ervin on 5/25/2017.
 */

public class UploadFile extends AppCompatActivity{
    private static final String TAG = "UploadFile";
    Button btnUpload, btnFilePicker;
    TextView namaFiel;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    String Path;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_file_layout);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        btnFilePicker = (Button) findViewById(R.id.fileChooser);
        btnUpload = (Button) findViewById(R.id.upload2) ;
        namaFiel = (TextView) findViewById(R.id.namaFile);
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        checkFilePermissions();

//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                Object value = dataSnapshot.getValue();
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

        btnFilePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity(UploadFile.this)
                        .withRequestCode(1)
                        .start();
//                Intent intent = new Intent(UploadFile.this, AlbumSelectActivity.class);
//                //intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, <LIMIT>); // set limit for image selection
//                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = namaFiel.getText().toString();
                Uri file = Uri.fromFile(new File(Path));
                StorageReference riversRef = mStorageRef.child("file/users/" + userID +"/"+ nama);

                riversRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                              @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                String linkFile = String.valueOf(downloadUrl);
                                myRef.child("file").child(userID).child("link file").setValue(linkFile);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Path=filePath;
            String filename=Path.substring(Path.lastIndexOf("/")+1);
            namaFiel.setText(filename);
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void checkFilePermissions() {

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){

            int permissionCheck = UploadFile.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");

            permissionCheck += UploadFile.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");

            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number

            }

        }else{

            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");

        }

    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
//            //The array list has the image paths of the selected images
//            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
//
//            for (int i = 0; i < images.size(); i++) {
//                Uri uri = Uri.fromFile(new File(images.get(i).path));
//                // start play with image uri
//               Path= String.valueOf(uri);
//
//            }
//            String noStrop = Path.replaceAll("file://","");
//            a=noStrop;
//            namaFiel.setText(a);
//        }
//    }

}
