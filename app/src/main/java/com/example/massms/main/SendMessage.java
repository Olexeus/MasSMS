package com.example.massms.main;

import android.Manifest;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.massms.R;
import com.example.massms.models.Group;
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

        if(getIntent().getIntExtra("Person", -1) != -1) {
            List<Person> person = new ArrayList<>();
            person.add(GroupManager.getGroup(getIntent().
                    getStringExtra("Group")).getContacts().get(getIntent().
                    getIntExtra("Person", 0)));
            ArrayAdapter<Person> itemsAdapter = new ArrayAdapter<Person>(this,
                    android.R.layout.simple_list_item_1, person);
            listView.setAdapter(itemsAdapter);
        }
        else{
            ArrayAdapter<Person> itemsAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, GroupManager.getGroup(getIntent().getStringExtra("Group")).getContacts());
            listView.setAdapter(itemsAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SendMessage.this, SendMessage.class);
                intent.putExtra("Group", GroupManager.getGroup(getIntent().getStringExtra("Group")).getGroupName());
                intent.putExtra("Person", position);
                SendMessage.this.startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessage(messageView.getText().toString(), GroupManager.getGroup(getIntent().getStringExtra("Group")),
                        getIntent().getIntExtra("Person", -1), getApplicationContext());
                messageView.getText().clear();
                Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete_group, menu);
        if(getIntent().getIntExtra("Person", -1) == -1) {
            getMenuInflater().inflate(R.menu.show_conversation_history, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_group) {
            GroupManager.deleteGroup(getIntent().getStringExtra("Group"));
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            return true;
        }

        if(id == R.id.show_history){
            if(item.isChecked()){
                item.setChecked(false);
                ArrayAdapter<Person> itemsAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, GroupManager.getGroup(getIntent().getStringExtra("Group")).getContacts());
                listView.setAdapter(itemsAdapter);
            }
            else{
                item.setChecked(true);
                ArrayAdapter<Message> itemsAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, GroupManager.getGroup(getIntent().getStringExtra("Group")).getHistory());
                listView.setAdapter(itemsAdapter);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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
