package com.example.massms.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.massms.models.Group;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SendPresenter implements SendContract.Presenter {
    private SendContract.View view;

    SendPresenter(SendContract.View view){ this.view = view; }

    @Override
    public void sendMessage(String message, Group group, int person, Context context) {
        if(message.length() > 160){
            sendLongMessage(message, group, person,  context);
        }
        else{
            sendShortMessage(message, group, person, context);
        }
    }

    private boolean checkPermission(Context context){
        int check = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
    private void sendShortMessage(String message, Group group, int person, Context context){
        if(person == -1) {
            // Sends to each contact in the group
            for (int i = 0; i < group.getContacts().size(); i++) {
                // We have to use BigDecimal because floats store in scientific notation
                String phoneNumber = (new BigDecimal(group.getContact(i).getPhone())).toString();

                if (phoneNumber.length() == 0 || message.length() == 0) {
                    Log.d("Blank values", phoneNumber + " " + message);
                    return;
                }

                // Checks permissions once again
                if (checkPermission(context)) {
                    Log.d("Sending Message", "Sending to: " + phoneNumber + " and message: "
                            + message);
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                } else {
                    Log.d("Permission Denied", "DENIED");
                }
            }
        }
        else{
            String phoneNumber = (new BigDecimal(group.getContact(person).getPhone())).toString();

            if (phoneNumber.length() == 0 || message.length() == 0) {
                Log.d("Blank values", phoneNumber + " " + message);
                return;
            }

            // Checks permissions once again
            if (checkPermission(context)) {
                Log.d("Sending Message", "Sending to: " + phoneNumber + " and message: "
                        + message);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            } else {
                Log.d("Permission Denied", "DENIED");
            }
        }
    }
    private void sendLongMessage(String message, Group group, int person, Context context){
        if(person != -1) {
            // Splits the message into a List of 160 characters each
            List<String> longMessage = new ArrayList<>();
            for (int i = 0; i < message.length(); i += 160) {
                longMessage.add(message.substring(i, Math.min(i + 160, message.length())));
            }
            for (int i = 0; i < longMessage.size(); i++) {
                Log.d("Outputting Message:", longMessage.get(i));
            }

            // Sends to each contact in the group
            for (int i = 0; i < group.getContacts().size(); i++) {
                // We have to use BigDecimal because floats store in scientific notation
                String phoneNumber = (new BigDecimal(group.getContact(i).getPhone())).toString();

                if (phoneNumber.length() == 0 || longMessage.size() == 0) {
                    Log.d("Blank values", phoneNumber + " " + longMessage);
                    return;
                }

                // Checks permissions once again
                if (checkPermission(context)) {
                    // Sends each 160 character string in the message
                    for (int j = 0; j < longMessage.size(); j++) {
                        Log.d("Sending Message", "Sending to: " + phoneNumber + " and message: "
                                + longMessage.get(j));
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, longMessage.get(i), null, null);
                    }
                } else {
                    Log.d("Permission Denied", "DENIED");
                }
            }
        }
        else{
            String phoneNumber = (new BigDecimal(group.getContact(person).getPhone())).toString();

            if (phoneNumber.length() == 0 || message.length() == 0) {
                Log.d("Blank values", phoneNumber + " " + message);
                return;
            }

            // Checks permissions once again
            if (checkPermission(context)) {
                Log.d("Sending Message", "Sending to: " + phoneNumber + " and message: "
                        + message);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            } else {
                Log.d("Permission Denied", "DENIED");
            }
        }
    }

    @Override
    public void onDestroy() {

    }
}
