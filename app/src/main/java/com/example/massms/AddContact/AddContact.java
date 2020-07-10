package com.example.massms.AddContact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.massms.R;
import com.example.massms.SendMessage.SendContract;
import com.example.massms.SendMessage.SendMessage;
import com.example.massms.models.GroupManager;
import com.example.massms.models.Person;

import java.util.List;

public class AddContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getSharedPreferences("DarkMode", MODE_PRIVATE);
        if (sharedPref.getBoolean("Dark", false)) {
            setTheme(R.style.DarkThemeWithActionBar);
        } else {
            setTheme(R.style.AppThemeWithActionBar);
        }

        setContentView(R.layout.activity_add_contact);

        final EditText name = findViewById(R.id.person_name);
        final EditText number = findViewById(R.id.person_number);
        Button save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Must enter name!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (number.getText().toString().trim().length() < 10) {
                    Toast.makeText(getApplicationContext(), "Phone number must be at least 10 numbers!", Toast.LENGTH_SHORT).show();
                    return;
                }
                GroupManager.getGroup(getIntent().getStringExtra("Group")).addContact(new
                        Person(name.getText().toString(), Double.parseDouble(number.getText().toString())));
                GroupManager.saveGroups();
                finish();
            }
        });
    }
}