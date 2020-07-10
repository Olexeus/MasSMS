package com.example.massms.models;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    public void createDirectory() {
        File directory;
        if (fileName.isEmpty()) {
            directory = context.getFilesDir();
        } else {
            directory = context.getDir(fileName, MODE_PRIVATE);
        }
        File[] files = directory.listFiles();
    }

    public String readFile(){
        //openFileInput reads data from internal storage.
        FileInputStream fis;
        Scanner scanner;
        String content = null;
        try {
            fis = context.openFileInput(fileName);
            scanner = new Scanner(fis);
            //A delimiter is most likely not going to be necessary.
            //scanner.useDelimiter("\\Z");
            String nextChar;
            while (scanner.hasNext()) {
                nextChar = scanner.next();
                if (content != null) {
                    content += nextChar;
                }
                else {
                    content = nextChar;
                }
            }
            scanner.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return content;
    }

    public void writeToFile(@org.jetbrains.annotations.NotNull String internalStorageBinding) {
        FileOutputStream fos = null;
        //openFileOutput writes data to internal storage.
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(internalStorageBinding.getBytes());
        } catch (IOException e){
            e.printStackTrace();
        } finally {
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