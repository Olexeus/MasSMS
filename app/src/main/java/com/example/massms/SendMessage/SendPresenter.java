package com.example.massms.SendMessage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.massms.models.Group;
import com.example.massms.models.GroupManager;
import com.example.massms.models.Message;

import java.util.Calendar;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendPresenter implements SendContract.Presenter {
    private SendContract.View view;

    SendPresenter(SendContract.View view){ this.view = view; }

    @Override
    public void sendMessage(final String message, final Group group, int person, final Context context) {
        if(person == -1) {
            Thread sendMessage = new Thread(new Runnable() {
                @Override
                public void run() {
                    // Sends to each contact in the group
                    for (int i = 0; i < group.getContacts().size(); i++) {
                        sendingMessage(message, group, i, context);
                    }
                    // Adds a new message to the conversation history for this group
                    group.addMessage(new Message(message, Calendar.getInstance()));
                    GroupManager.saveGroups();
                }
            });
            sendMessage.start();
            // Alerts the user when finished
            Toast.makeText(context, "Messages sent", Toast.LENGTH_SHORT).show();
        }
        else{
            sendingMessage(message, group, person, context);
            // Alerts the user when finished
            Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean canSendSMS(Context context){
        // Checks if permission has been granted to the app to send messages
        int check = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    private void sendingMessage(String message, Group group, int person, Context context){
        // We have to use BigDecimal because floats store in scientific notation
        String phoneNumber = (new BigDecimal(group.getContact(person).getPhone())).toString();

        // Stops users from accidentally sending blank messages
        if (phoneNumber.length() == 0 || message.length() == 0) {
            return;
        }

        // Checks permissions once again
        if (canSendSMS(context)) {
            SmsManager smsManager = SmsManager.getDefault();
            // Sends text
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } else {
            Log.e("Permission Denied", "DENIED");
        }
    }

    @Override
    public void onDestroy() {

    }
}
