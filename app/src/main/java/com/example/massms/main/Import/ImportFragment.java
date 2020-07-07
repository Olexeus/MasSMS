package com.example.massms.main.Import;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.massms.R;
import com.example.massms.models.GroupManager;

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

        final EditText editText = view.findViewById(R.id.editText);
        Button finish = view.findViewById(R.id.finish);

        editText.setVisibility(View.INVISIBLE);
        finish.setVisibility(View.INVISIBLE);

        importGroup();

        ((EditText)view.findViewById(R.id.editText)).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                GroupManager.getGroups().get(GroupManager.getSize() - 1).addName(editText.getText().toString());
                                GroupManager.saveGroups();
                                NavHostFragment.findNavController(ImportFragment.this)
                                        .navigate(R.id.action_import_to_list);
                                return true;
                            }
                        }
                        return false;
                    }
                }
        );

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Testing GroupManager
                // TODO: Check if thread is finished and then add name
                GroupManager.getGroups().get(GroupManager.getSize() - 1).addName(editText.getText().toString());
                GroupManager.saveGroups();
                NavHostFragment.findNavController(ImportFragment.this)
                        .navigate(R.id.action_import_to_list);
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
        if(data == null){
            NavHostFragment.findNavController(ImportFragment.this)
                    .navigate(R.id.action_import_to_list);
        }
        try {
            super.onActivityResult(requestCode, resultCode, data);

            EditText editText = getView().findViewById(R.id.editText);
            Button finish = getView().findViewById(R.id.finish);

            editText.setVisibility(View.VISIBLE);
            finish.setVisibility(View.VISIBLE);

            if (requestCode == 1 && resultCode == -1) {
                // Not sure if this should be where this is called
                presenter.convertExcelToJson(data, getContext());
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
