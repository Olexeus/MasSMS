package com.example.massms.models;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static android.content.Context.MODE_PRIVATE;

public class DataManager{
    String fileName = "masssms";
    Context context;

    public DataManager(Context context){
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void createDirectory(Context context) {
        File directory;
        if (fileName.isEmpty()) {
            directory = context.getFilesDir();
        } else {
            directory = context.getDir(fileName, MODE_PRIVATE);
        }
        File[] files = directory.listFiles();
    }

    public void readFile(Context context){
        //openFileInput reads data from internal storage.
        FileInputStream fis = null;
        Scanner scanner = null;
        try {
            fis = context.openFileInput(fileName);
            scanner = new Scanner(fis);
            //A delimiter is most likely not going to be necessary.
            //scanner.useDelimiter("\\Z");
            String content = scanner.next();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        scanner.close();
    }
    public void writeToFile(Context context, JsonObject internalStorageBinding) {

        FileOutputStream fos = null;
        //openFileOutput writes data to internal storage.
        try {
            fos = context.openFileOutput(fileName, MODE_PRIVATE);
            fos.write(internalStorageBinding.toString().getBytes());
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (fos != null){
                try{
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

    }

}