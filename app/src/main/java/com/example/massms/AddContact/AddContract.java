package com.example.massms.AddContact;

import android.content.Context;

import com.example.massms.BasePresenter;
import com.example.massms.BaseView;
import com.example.massms.SendMessage.SendContract;
import com.example.massms.models.Group;

public interface AddContract {
    interface Presenter extends BasePresenter {
        void saveContact(String name, float number);
    }

    interface View extends BaseView<SendContract.Presenter> {
        // We will add any "display-like" methods that the Presenter should know about here.
        //void display();
    }
}
