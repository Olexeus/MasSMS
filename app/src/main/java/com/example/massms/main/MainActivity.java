package com.example.massms.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.massms.R;
import com.example.massms.models.GroupManager;
import com.google.gson.JsonObject;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    // Add a presenter property. The view needs the presenter to invoke user initiated callbacks.
    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
