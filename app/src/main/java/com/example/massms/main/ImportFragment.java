package com.example.massms.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.massms.models.Group;
import com.example.massms.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImportFragment extends Fragment implements ImportContract.View {
    ImportContract.Presenter presenter;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // I'm not sure whether these 2 methods would go in here or onViewCreated since I still don't fully understand fragments.
        setPresenter(new ImportPresenter(this));
        presenter.onViewCreated();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.import_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importGroup();
            }
        });
    }

    private void importGroup() {
        String[] mimeTypes =
                { "application/vnd.ms-excel",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 1);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1 && resultCode == -1) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JsonObject jsonObject;
                        Uri uri = data.getData();
                        try(InputStream inputStream = getContext().getContentResolver().openInputStream(uri)) {
                            // getExcelDataAsJsonObject is probably too expensive to run on the ui thread
                            MainActivity.excelJsonObject = getExcelDataAsJsonObject(inputStream);
                            Log.d("First Fragment", MainActivity.excelJsonObject.toString());

                            Gson gson = new Gson();
                            Group newGroup = new Group();

                            // TODO: use commented string when conversion is fixed
                            // String JsonString = MainActivity.excelJsonObject.toString();
                            String JsonString = "{\"Ward\":[{\"first\":\"Zach\",\"last\":\"Peterson\",\"phone\":9.132719504E9},{\"first\":\"Elder1\",\"last\":\"Smith\",\"phone\":5.551112222E9},{\"first\":\"Deacon1\",\"last\":\"Smith\",\"phone\":4.441113333E9}]}";

                            // Creates a new Group class
                            newGroup = gson.fromJson(JsonString, Group.class);
                            Log.d("Group object", newGroup.toString());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, "ExcelToJsonThread").start();

                NavHostFragment.findNavController(ImportFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public JsonObject getExcelDataAsJsonObject(InputStream inputStream) {

        JsonObject sheetsJsonObject = new JsonObject();

        Workbook workbook = null;
        try {
            //workbook = WorkbookFactory.create(inputStream);
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
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
    public void display() {

    }

    // We never construct a MainActivity so we call this in onCreate to store a reference.
    @Override
    public void setPresenter(ImportContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
