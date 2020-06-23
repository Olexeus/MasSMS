package com.example.massms.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.massms.R;
import com.example.massms.models.Group;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListFragment extends Fragment implements ListContract.View {
    private ListContract.Presenter presenter;
    public static String groupListString = "0";
    public static String startingTextString = "0";
    TextView groupList;
    TextView startingText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setPresenter(new ListPresenter(this));
        presenter.onViewCreated();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        groupList = view.findViewById(R.id.group_list);

        startingText = view.findViewById(R.id.starting_text);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ListFragment.this)
                        .navigate(R.id.action_list_to_import);
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        if(!groupListString.equals("0")){
            groupList.setVisibility(View.VISIBLE);
            groupList.setText(MainActivity.group.getGroupName());
            startingText.setText(R.string.add_more);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void setPresenter(ListContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
