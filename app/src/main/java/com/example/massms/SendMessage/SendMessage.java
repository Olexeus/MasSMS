package com.example.massms.SendMessage;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.massms.AddContact.AddContact;
import com.example.massms.R;
import com.example.massms.main.MainActivity;
import com.example.massms.models.GroupManager;
import com.example.massms.models.Message;
import com.example.massms.models.Person;

import java.util.ArrayList;
import java.util.List;

public class SendMessage extends AppCompatActivity implements SendContract.View {
    private SendContract.Presenter presenter;
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    EditText messageView;
    Button send;
    ListView listView;
    String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setPresenter(new SendPresenter(this));
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getSharedPreferences("DarkMode", MODE_PRIVATE);
        if (sharedPref.getBoolean("Dark", false)) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_send_message);

        messageView = findViewById(R.id.message_view);
        send = findViewById(R.id.send);
        listView = findViewById(R.id.contact_view);
        groupName = getIntent().getStringExtra("Group");

        // Checks permissions when the activity is started for the first time
        if(canSendSMS()){
            send.setEnabled(true);
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        // We wrap the list view click listener in an if statement so we can select an individual
        // from the group if we're displaying a group, but disable eternal activity launching
        // if we're seeing an individual
        if(getIntent().getIntExtra("Person", -1) == -1) {
            // This allows us to use this activity to display either a group or an individual
            // We set an item click listener that passes the person to a new instance of this
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(SendMessage.this, SendMessage.class);
                    intent.putExtra("Group", GroupManager.getGroup(groupName).getGroupName());
                    intent.putExtra("Person", position);
                    SendMessage.this.startActivity(intent);
                }
            });
        }

        // Sends a message to either the group or and individual. -1 means the group, anything else
        // is a person
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessage(messageView.getText().toString(), GroupManager.getGroup(groupName),
                        getIntent().getIntExtra("Person", -1), getApplicationContext());
                messageView.getText().clear();
            }
        });
    }

    /**
     * On Resume checks what kind of data to put in the list view
     * (a single person or a whole group). We enhance reuse-ability by
     * using a singe list-view.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Checks if the list view needs to display a single person
        if(getIntent().getIntExtra("Person", -1) != -1) {
            // The array adapter is expecting a list, so we add the single person
            // to that list and pass that to the array adapter
            List<Person> person = new ArrayList<>();
            person.add(GroupManager.getGroup(getIntent().
                    getStringExtra("Group")).getContacts().get(getIntent().
                    getIntExtra("Person", -1)));
            ArrayAdapter<Person> itemsAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, person);
            listView.setAdapter(itemsAdapter);
        }
        // Displays a group of people
        else{
            ArrayAdapter<Person> itemsAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, GroupManager.getGroup(groupName).getContacts());
            listView.setAdapter(itemsAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Adds items to the menu if we're seeing the whole group
        if(getIntent().getIntExtra("Person", -1) == -1) {
            getMenuInflater().inflate(R.menu.add_contact, menu);
            getMenuInflater().inflate(R.menu.delete_group, menu);
            getMenuInflater().inflate(R.menu.show_conversation_history, menu);
        }
        // Adds items to the menu if we're seeing an individual
        else{
            getMenuInflater().inflate(R.menu.delete_contact, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handles menu selection
        int id = item.getItemId();

        // Delete the group and return to the main screen
        if (id == R.id.delete_group) {
            GroupManager.deleteGroup(groupName);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            return true;
        }

        if(id == R.id.show_history){
            // If show history is already checked, uncheck and show people
            if(item.isChecked()){
                item.setChecked(false);
                ArrayAdapter<Person> itemsAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, GroupManager.getGroup(groupName).getContacts());
                listView.setAdapter(itemsAdapter);
            }
            // Else, check, and show conversations
            else{
                item.setChecked(true);
                ArrayAdapter<Message> itemsAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, GroupManager.getGroup(groupName).getHistory());
                listView.setAdapter(itemsAdapter);
            }
            return true;
        }

        // Deletes the contact and end the activity
        if(id == R.id.delete_contact){
            int index = getIntent().getIntExtra("Person", -1);
            GroupManager.getGroup(groupName).deleteContact(index);
            GroupManager.saveGroups();
            finish();
            return true;
        }

        // Launches a new Add Contact activity
        if(id == R.id.add_contact){
            Intent intent = new Intent(SendMessage.this, AddContact.class);
            intent.putExtra("Group", groupName);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(SendContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Checks if app has permission to send SMSs
     * @return if app can send SMSs
     */
    private boolean canSendSMS(){
        int check = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
