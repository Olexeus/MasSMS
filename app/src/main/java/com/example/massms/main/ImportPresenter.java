package com.example.massms.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.TimingLogger;

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

public class ImportPresenter implements ImportContract.Presenter {
    private ImportContract.View view;
    private static final String TAG = "ImportPresenter";

    // Constructor takes in a view, along with any other dependencies
    ImportPresenter(ImportContract.View view) {
        this.view = view;
    }

    // Called by the view when it is created
    @Override
    public void onViewCreated() {

    }

    @Override
    public void convertExcelToJson(final Intent data, final Context context) {
        // TODO: Clean this up.
        Thread ExcelToJsonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = data.getData();
                try(InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
                    // getExcelDataAsJsonObject is probably too expensive to run on the ui thread
                    // TODO: DataManager should deal with this.
                    // TODO: check timing
                    // adb shell setprop log.tag.ExcelToJSON VERBOSE
                    TimingLogger timings = new TimingLogger("ExcelToJSON", "");
                    MainActivity.excelJsonObject = getExcelDataAsJsonObject(inputStream);
                    Log.d("First Fragment", MainActivity.excelJsonObject.toString());
                    timings.addSplit("Excel to JSON");

                    // Creates a new Group class
                    // TODO: sort out this mess
                    Group newGroup = Group.fromJson(MainActivity.excelJsonObject);
                    GroupManager.addGroup(newGroup);
                    timings.addSplit("JSON to Object");
                    timings.dumpToLog();
                    Log.d("Group object", newGroup.toString());

                } catch (IOException e) {
                    Log.d(TAG, "Somethings wrong lol");
                }
            }
        }, "ExcelToJsonThread");
        ExcelToJsonThread.start();
    }

    // Called by the view when it is destroyed
    @Override
    public void onDestroy() {
    }

    private JsonObject getExcelDataAsJsonObject(InputStream inputStream) {
        JsonObject sheetsJsonObject = new JsonObject();
        Workbook workbook = null;

        try {
            // I think this is better for some reason. Leaving the other just in case.
            workbook = WorkbookFactory.create(inputStream);
            //workbook = new XSSFWorkbook(inputStream);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }

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
