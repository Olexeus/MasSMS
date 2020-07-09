package com.example.massms.SendMessage;

import android.content.Context;

import com.example.massms.BasePresenter;
import com.example.massms.BaseView;
import com.example.massms.models.Group;

/**
 * This interface allows us to implement MVP design. The view can access the presenter through this
 */
public interface SendContract {
    interface Presenter extends BasePresenter{
        void sendMessage(String message, Group group, int person, Context context);
    }

    interface View extends BaseView<SendContract.Presenter> {

    }
}
