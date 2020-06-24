package com.example.massms;


public class DataManager{
/*
    public void createDirectory() {
        File directory;
        if (filename.isEmpty()) {
            directory = getFilesDir();
        } else {
            directory = getDir(filename, MODE_PRIVATE);
        }
        File[] files = directory.listFiles();
    }

    public void readFile(){
        //openFileInput reads data from internal storage.
        FileInputStream fis = null;
        try {
            fis = openFileInput(filename);
            Scanner scanner = new Scanner(fis);
            scanner.useDelimiter("\\Z");
            String content = scanner.next();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (scanner != null){
                scanner.close();
            }
        }

        //scanner.close();
    }
    public void writeToFile() {
        FileOutputStream fos;
        //openFileOutput writes data to internal storage.
        try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(internalStorageBinding.saveFileEditText.getText().toString().getBytes());
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

        //fos.close();
    }
    */
}