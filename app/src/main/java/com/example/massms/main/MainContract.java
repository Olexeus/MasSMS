package com.example.massms.main;

import com.example.massms.BasePresenter;
import com.example.massms.BaseView;

/**
 * This interface allows us to implement MVP design. The view can access the presenter through this
 */

public interface MainContract {
    interface Presenter extends BasePresenter {
        // This is called in the Activity's or Fragment's onCreate method.
        void onViewCreated();
    }

    interface View extends BaseView<Presenter> {

    }
}
