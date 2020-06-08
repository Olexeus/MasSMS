package com.example.massms.main;

import com.example.massms.BasePresenter;
import com.example.massms.BaseView;

public interface ListContract {
    interface Presenter extends BasePresenter {
        void onViewCreated();
    }

    interface View extends BaseView<Presenter> {
        void display();
    }
}
