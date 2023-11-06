package com.byt3.byaudio.controller;

import static com.byt3.byaudio.utils.functions.checkFolderName;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.byt3.byaudio.R;
import com.byt3.byaudio.model.AppDatabase;
import com.byt3.byaudio.model.dbhandler.FolderDAO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    final int REQUEST_PERMISSION_READING_STATE = 1;
    private static final String RequiredPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    List<String> myList = new ArrayList<>();
    List<String> folderName = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        HandlePermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateDB();
    }

    private void nextActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }

    private void updateDB(){
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "database")
                .build();
        FolderDAO folderDAO = db.folderDAO();

        File file = Environment.getExternalStorageDirectory();
        filterFolder(file);
        System.out.println(folderName);
        if (myList!=null)
            System.out.println(myList.size());
    }
    private void filterFolder(File file){
        File[] list = file.listFiles();
        if (list != null){
            for (File f : list){
                if (checkFolderName(f.getName()))
                    searchValidFolder(f, f.getName());
            }
        }
    }
    private void searchValidFolder(File file, String directory){
        File[] list = file.listFiles();
        boolean folderFound = false;
        File mFile = null;
        String directoryName = "";

        if (list != null) {
            for (File value : list) {
                mFile = new File(file, value.getName());
                if (mFile.isDirectory()) {
                    directoryName = value.getName();
                    searchValidFolder(mFile, directoryName);
                } else {
                    if (value.getName().toLowerCase().endsWith(".mp3")) {
                        myList.add(value.getName());
                        folderFound = true;
                    }
                }
            }
        }
        if (folderFound)
            folderName.add(directory);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("amir", "onRequestPermissionsResult: called");
        if (requestCode == REQUEST_PERMISSION_READING_STATE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SplashActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(this::nextActivity, 2000);
            } else {
                Toast.makeText(SplashActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(this::nextActivity, 2000);
            }
        }
    }
    private void HandlePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, RequiredPermission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, RequiredPermission))
                showExplanationAboutPermission();
            else
                ActivityCompat.requestPermissions(this,
                        new String[]{SplashActivity.RequiredPermission},
                        1);
        else{
            Toast.makeText(SplashActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(this::nextActivity, 2000);
        }
    }

    private void showExplanationAboutPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Needed")
                .setMessage("Rationale")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(SplashActivity.this,
                                new String[]{SplashActivity.RequiredPermission}, 1);
                    }
                });
        builder.create().show();
    }
}
