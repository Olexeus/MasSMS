package com.example.massms.main.List;

import com.example.massms.BasePresenter;
import com.example.massms.BaseView;

/**
 * This interface allows us to implement MVP design. The view can access the presenter through this
 */

public interface ListContract {
    interface Presenter extends BasePresenter {
        // This is called when the view is created
        void onViewCreated();
    }

    interface View extends BaseView<Presenter> {

    }
}
