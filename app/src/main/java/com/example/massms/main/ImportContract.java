package com.example.massms.main;

import android.content.Context;
import android.content.Intent;

import com.example.massms.BasePresenter;
import com.example.massms.BaseView;

// This is the contract between our view and presenter
// TODO : add more methods :-)
public interface ImportContract {
    interface Presenter extends BasePresenter {
        // This is called in the Activity's or Fragment's onCreate method.
        void onViewCreated();
        void convertExcelToJson(final Intent data, final Context context);
        Intent createImportIntent();
    }

    interface View extends BaseView<Presenter> {
        // We will add any "display-like" methods that the Presenter should know about here.
        //void display();
    }
}
