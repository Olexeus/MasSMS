package com.example.massms.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.massms.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ImportFragment extends Fragment implements ImportContract.View {
    private ImportContract.Presenter presenter;
    private String groupName;
    private Button importButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // I'm not sure whether these 2 methods would go in here or onViewCreated since I still don't fully understand fragments.
        setPresenter(new ImportPresenter(this));
        presenter.onViewCreated();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.import_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editText = view.findViewById(R.id.editText);
        Button finish = view.findViewById(R.id.finish);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importGroup();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        EditText editText = getView().findViewById(R.id.editText);
        groupName = editText.getText().toString();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1 && resultCode == -1) {
                // Not sure if this should be where this is called
                presenter.convertExcelToJson(data, getContext(), groupName);
                ListFragment.groupListString = MainActivity.group.getGroupName();
                ListFragment.startingTextString = getString(R.string.add_more);
                NavHostFragment.findNavController(ImportFragment.this)
                        .navigate(R.id.action_import_to_list);

            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    // We never construct a MainActivity so we call this in onCreate to store a reference.
    @Override
    public void setPresenter(ImportContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void importGroup() {
        Intent intent = presenter.createImportIntent();
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 1);
    }
}
