package com.example.massms.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.massms.AddContact.AddContact;
import com.example.massms.R;
import com.example.massms.SendMessage.SendMessage;
import com.example.massms.models.GroupManager;
import com.example.massms.models.Message;
import com.example.massms.models.Person;
import com.google.gson.JsonObject;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    // Add a presenter property. The view needs the presenter to invoke user initiated callbacks.
    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getSharedPreferences("DarkMode", MODE_PRIVATE);
        if (sharedPref.getBoolean("Dark", false)) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Store a reference to the presenter just after creating it.
        setPresenter(new MainPresenter(this));
        presenter.onViewCreated();

        // Instantiate GroupManager
        // Most of this class is static. We just need to instantiate it once in the MainActivity
        // Otherwise it will not work properly
        new GroupManager(this.getApplicationContext());

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    // We never construct a MainActivity so we call this in onCreate to store a reference.
    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handles menu selection
        int id = item.getItemId();

        SharedPreferences sharedPref = getSharedPreferences("DarkMode", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Delete the group and return to the main screen
        if (id == R.id.DarkMode) {
            if (sharedPref.getBoolean("Dark", false)) {
                editor.putBoolean("Dark", false);
                recreate();
            } else {
                editor.putBoolean("Dark", true);
                recreate();
            }
            editor.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
