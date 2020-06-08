package com.example.massms.main;

import com.example.massms.BasePresenter;
import com.example.massms.BaseView;

public interface MainContract {
    interface Presenter extends BasePresenter {
        // This is called in the Activity's or Fragment's onCreate method.
        void onViewCreated();

        // TODO : add more methods :-)
    }

    interface View extends BaseView<Presenter> {
        // We will add any "display-like" methods that the Presenter should know about here.
        void display();
    }
}
