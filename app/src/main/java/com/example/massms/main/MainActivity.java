package com.example.massms.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.massms.R;
import com.example.massms.models.Group;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Remember that the view should be as dumb as possible.
 * Views only handle what is displayed on screen and user interaction, like a click listener.
 * Views should not contain any business logic, they only display things.
 *  TODO : Everything must be "MVPitized".
 */
public class MainActivity extends AppCompatActivity implements MainContract.View {
    // TODO: Move Data.java Stuff to a model
    public static JsonObject excelJsonObject = null;

    // TODO: When we finish with the DataManager, we need to decouple this from ImportPresenter
    public static Group group;

    // Add a presenter property. The view needs the presenter to invoke user initiated callbacks.
    private MainContract.Presenter presenter;

    // TODO: create list of Group. Maybe class that reads the JSON file with links to all the groups
    // TODO: function to add the Groups to the GroupList class (or whatever it is called)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Store a reference to the presenter just after creating it.
        setPresenter(new MainPresenter(this));
        presenter.onViewCreated();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        getMenuInflater().inflate(R.menu.import_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.import_contacts) {
            Toast.makeText(this, "Click the \"Import\" button to get started", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}
