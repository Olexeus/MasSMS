package com.example.massms.main.Import;

import android.content.Context;
import android.content.Intent;

import com.example.massms.BasePresenter;
import com.example.massms.BaseView;

/**
 * This interface allows us to implement MVP design. The view can access the presenter through this
 */

public interface ImportContract {
    interface Presenter extends BasePresenter {
        // This is called in the Activity's or Fragment's onCreate method.
        void onViewCreated();
        // This handles the conversion once the Excel file is received
        void convertExcelToJson(final Intent data, final Context context);
        // This is to determine the type of file to import
        Intent createImportIntent();
    }

    interface View extends BaseView<Presenter> {

    }
}
