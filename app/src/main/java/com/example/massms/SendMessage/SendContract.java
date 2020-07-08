package com.example.massms.SendMessage;

import android.content.Context;

import com.example.massms.BasePresenter;
import com.example.massms.BaseView;
import com.example.massms.models.Group;

public interface SendContract {
    interface Presenter extends BasePresenter{
        void sendMessage(String message, Group group, int person, Context context);
    }

    interface View extends BaseView<SendContract.Presenter> {
        // We will add any "display-like" methods that the Presenter should know about here.
        //void display();
    }
}
