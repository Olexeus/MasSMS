package com.example.massms.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.massms.R;
import com.example.massms.models.Group;
import com.example.massms.models.Person;

public class SendMessage extends AppCompatActivity implements SendContract.View {
    private SendContract.Presenter presenter;
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    EditText messageView;
    Button send;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setPresenter(new SendPresenter(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        messageView = findViewById(R.id.message_view);
        send = findViewById(R.id.send);
        listView = findViewById(R.id.contact_view);

        if(checkPermission()){
            send.setEnabled(true);
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        ArrayAdapter<Person> itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, MainActivity.group.getContacts());
        listView.setAdapter(itemsAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessage(messageView.getText().toString(), MainActivity.group, getApplicationContext());
                messageView.getText().clear();
                Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setPresenter(SendContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private boolean checkPermission(){
        int check = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}
