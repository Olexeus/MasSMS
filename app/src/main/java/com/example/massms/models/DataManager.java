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

    //Constructor
    public DataManager(Context context){
        this.context = context;
    }

    //This method makes sure we have a directory created on the phone to save the app data. In case there isn't, one shall be created.
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

    //readFile will read the file so that it can be used to access, modify, or delete any values from memory.
    public String readFile(){
        FileInputStream fis;
        Scanner scanner;
        String content = null;
        try {
            fis = context.openFileInput(fileName);
            scanner = new Scanner(fis);

            //The while loop is necessary to iterate over all the characters in memory.
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
    //This method makes sure that all changes made to the file after we read it are saved back to the phone's memory. The method will overwrite everything.
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