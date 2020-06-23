package com.example.massms.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.massms.R;
import com.example.massms.models.Group;

public class SendMessage extends AppCompatActivity implements SendContract.View {
    private SendContract.Presenter presenter;
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    EditText messageView;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setPresenter(new SendPresenter(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        messageView = findViewById(R.id.message_view);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessage(messageView.getText().toString(), MainActivity.group, getApplicationContext());
            }
        });
    }

    @Override
    public void setPresenter(SendContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
