package com.example.massms.main.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.massms.R;
import com.example.massms.SendMessage.SendMessage;
import com.example.massms.models.Group;
import com.example.massms.models.GroupManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements ListContract.View {
    private ListContract.Presenter presenter;
    private ListView groupList;
    private TextView startingText;

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

        // Moves to the import fragment when the fab is selected
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ListFragment.this)
                        .navigate(R.id.action_list_to_import);
            }
        });

        // Opens the send message activity when a group is selected
        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SendMessage.class);
                intent.putExtra("Group", GroupManager.getGroupNames().get(position));
                ListFragment.this.startActivity(intent);
            }
        });

    }

    /**
     * On Resume handles the logic for showing the starting hint
     * If the user has imported a group before, then hide it
     */
    @Override
    public void onResume(){
        super.onResume();
        if(GroupManager.getSize() != 0){
            groupList.setVisibility(View.VISIBLE);
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, GroupManager.getGroupNames());
            groupList.setAdapter(itemsAdapter);
            startingText.setVisibility(View.INVISIBLE);
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
