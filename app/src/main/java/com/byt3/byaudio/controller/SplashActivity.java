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
import com.byt3.byaudio.model.Folder;
import com.byt3.byaudio.model.dbhandler.FolderDAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    final int REQUEST_PERMISSION_READING_STATE = 1;
    private static final String RequiredPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    List<String> myList = new ArrayList<>();
    List<String> folderName = new ArrayList<>();
    List<Folder> folderList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        HandlePermission();
//        updateDB();
        firstRuntimeDataPopulate(Environment.getExternalStorageDirectory());
    }

    private void nextActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }

    private void updateDB(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, RequiredPermission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            AppDatabase db = Room.databaseBuilder(
                            getApplicationContext(),
                            AppDatabase.class,
                            "database")
                    .build();
            FolderDAO folderDAO = db.folderDAO();
            List<Folder> folders = folderDAO.getAll();
            for (Folder f : folders){
                File file = new File(f.getPath());
                try {
                    if (f.getSize() == file.listFiles().length){
                        file.getCanonicalPath();
                    }
                } catch (NullPointerException e){
                    Log.e("MyLog","Folder "+f.getName()+" is empty, deleting...");

                } catch (IOException e) {
                    Log.e("MyLog", "file IOE");
                    throw new RuntimeException(e);
                }
            }

            System.out.println(folderName);
            if (myList!=null)
                System.out.println(myList.size());
        }
    }
    private void firstRuntimeDataPopulate(File file){
        File[] list = file.listFiles();
        if (list != null){
            for (File f : list){
                if (checkFolderName(f.getName())) {
                    try {
                        searchValidFolder(f, f.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        if (folderList != null){
            AppDatabase db = Room.databaseBuilder(
                            getApplicationContext(),
                            AppDatabase.class,
                            "database")
                    .build();
            FolderDAO folderDAO = db.folderDAO();
            for (Folder f : folderList){
                Log.d("MyLog", f.getName()+" "+f.getSize()+" "+f.getPath());
                db.folderDAO().insertAll(f);
            }
        }
    }
    private void searchValidFolder(File file, String directory) throws IOException {
        File[] list = file.listFiles();
        boolean folderFound = false;
        File mFile = null;
        String directoryName = "";
        int counter = 0;
        if (list != null) {
            for (File value : list) {
                mFile = new File(file, value.getName());
                if (mFile.isDirectory()) {
                    directoryName = value.getName();
                    searchValidFolder(mFile, directoryName);
                } else {
                    if (value.getName().toLowerCase().endsWith(".mp3")) {
                        ++counter;
                        myList.add(value.getName());
                        folderFound = true;
                    }
                }
            }
        }
        if (folderFound){
            folderList.add(new Folder(directory, file.getCanonicalPath(), counter));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("MyLog", "onRequestPermissionsResult: called");
        if (requestCode == REQUEST_PERMISSION_READING_STATE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SplashActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
//                firstRuntimeDataPopulate(Environment.getExternalStorageDirectory());
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
