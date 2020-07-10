package com.example.massms.main.Import;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.TimingLogger;

import com.example.massms.main.MainActivity;
import com.example.massms.models.Group;
import com.example.massms.models.GroupManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * The Presenter holding all the functions for our Import view
 */

public class ImportPresenter implements ImportContract.Presenter {
    private ImportContract.View view;
    private static final String TAG = "ImportPresenter";
    private static JsonObject excelJsonObject = null;

    // Constructor takes in a view, along with any other dependencies
    ImportPresenter(ImportContract.View view) {
        this.view = view;
    }

    // Called by the view when it is created
    @Override
    public void onViewCreated() {
    }

    /**
     * Converts an Excel Object into a JSON object, which is saved
     * to a new group and added via Group Manager to the list of
     * Groups
     * @param data the Intent data
     * @param context the application context (to get the Excel data)
     */
    @Override
    public void convertExcelToJson(final Intent data, final Context context) {
        // Runs the task of converting to JSON on a separate thread
        Thread ExcelToJsonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = data.getData();
                try(InputStream inputStream = context.getContentResolver().openInputStream(uri)) {

                    excelJsonObject = getExcelDataAsJsonObject(inputStream);

                    // Creates a new Group instance
                    Group newGroup = Group.fromJson(excelJsonObject);
                    GroupManager.addGroup(newGroup);

                } catch (IOException e) {
                    Log.d(TAG, e.toString());
                }
            }
        }, "ExcelToJsonThread");
        ExcelToJsonThread.start();
    }

    // Called by the view when it is destroyed
    @Override
    public void onDestroy() {
        this.view = null;
    }

    /**
     * Changes the Excel data into a JSON object
     * @param inputStream The data of the Excel file
     * @return the JSON object that represents the Excel file
     */
    private JsonObject getExcelDataAsJsonObject(InputStream inputStream) {

        JsonObject sheetsJsonObject = new JsonObject();
        Workbook workbook = null;

        try {
            // Creates a new Excel workbook from the input stream
            workbook = WorkbookFactory.create(inputStream);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }

        // Nested for loops to get each column of each row
        // Adds each row to the JSON Object as it converts
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

            JsonArray sheetArray = new JsonArray();
            ArrayList<String> columnNames = new ArrayList<>();
            Sheet sheet = workbook.getSheetAt(i);

            for (Row currentRow : sheet) {
                JsonObject jsonObject = new JsonObject();

                if (currentRow.getRowNum() != 0) {

                    for (int j = 0; j < columnNames.size(); j++) {

                        if (currentRow.getCell(j) != null) {
                            if (currentRow.getCell(j).getCellTypeEnum() == CellType.STRING) {
                                jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getStringCellValue());
                            } else if (currentRow.getCell(j).getCellTypeEnum() == CellType.NUMERIC) {
                                jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getNumericCellValue());
                            } else if (currentRow.getCell(j).getCellTypeEnum() == CellType.BOOLEAN) {
                                jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getBooleanCellValue());
                            } else if (currentRow.getCell(j).getCellTypeEnum() == CellType.BLANK) {
                                jsonObject.addProperty(columnNames.get(j), "");
                            }
                        } else {
                            jsonObject.addProperty(columnNames.get(j), "");
                        }
                    }
                    sheetArray.add(jsonObject);
                } else {
                    // store column names
                    for (int k = 0; k < currentRow.getPhysicalNumberOfCells(); k++) {
                        columnNames.add(currentRow.getCell(k).getStringCellValue());
                    }
                }
            }
            sheetsJsonObject.add(workbook.getSheetName(i), sheetArray);
        }
        return sheetsJsonObject;
    }

    /**
     * Creates an intent that get's Excel data.
     * This function is here so it is trivial if we need to
     * change the type of data the Intent is retrieving
     * @return an intent that the fragment can use
     */
    @Override
    public Intent createImportIntent() {
        Intent intent;
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = { "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        return intent;
    }
}
