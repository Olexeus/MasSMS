package com.example.massms.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.massms.models.Group;

import java.math.BigDecimal;

public class SendPresenter implements SendContract.Presenter {
    private SendContract.View view;

    SendPresenter(SendContract.View view){ this.view = view; }

    @Override
    public void sendMessage(String message, Group group, Context context) {
        // TODO: Add logic to split message up if it's longer than 160 chars
        for(int i = 0; i < MainActivity.group.getContacts().size(); i++)
        {
            String phoneNumber = (new BigDecimal(MainActivity.group.getContact(i).getPhone())).toString();

            if(phoneNumber.length() == 0 || message.length() == 0){
                Log.d("Blank values", phoneNumber + " " + message);
                return;
            }

            if(checkPermission(context)){

                Log.d("Sending Message", "Sending to: " + phoneNumber + " and message: "
                        + message);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            }else{
                Log.d("Permission Denied", "DENIED");
            }
        }

    }

    private boolean checkPermission(Context context){
        int check = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onDestroy() {

    }
}
