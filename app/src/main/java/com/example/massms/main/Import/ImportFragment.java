package com.example.massms.main.Import;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.massms.R;
import com.example.massms.models.GroupManager;

import java.util.Timer;

public class ImportFragment extends Fragment implements ImportContract.View {
    private ImportContract.Presenter presenter;
    private EditText editText;
    private Button finish;
    long startTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setPresenter(new ImportPresenter(this));
        presenter.onViewCreated();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.import_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = view.findViewById(R.id.editText);
        finish = view.findViewById(R.id.finish);

        // Set to invisible because launching a new intent is noticeably laggy
        // Instead, the user sees a blank screen for a split second (as if it's loading)
        editText.setVisibility(View.INVISIBLE);
        finish.setVisibility(View.INVISIBLE);

        // The first thing when this fragment is created is launch an intent to find the Excel file
        importGroup();

        // Finish importing if the user hits "enter" on their keyboard
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
                                finishImport();
                                return true;
                            }
                        }
                        return false;
                    }
                }
        );

        // Finish importing if the user hits finish
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishImport();
            }
        });
    }

    /**
     * This function is called after the Excel file is selected by the user
     * @param requestCode Stuff that comes with the function
     * @param resultCode Stuff that comes with the function
     * @param data Stuff that comes with the function
     */
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            // Sets the views back to visible when the intent is finished
            editText.setVisibility(View.VISIBLE);
            finish.setVisibility(View.VISIBLE);

            // If the StartActivityForResult got something
            if (requestCode == 1 && resultCode == -1) {
                presenter.convertExcelToJson(data, getContext());
            }
            // If the user pressed "back", abort back to the main screen
            else{
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

    /**
     * Creates the intent to find the Excel file. This must be called in this fragment
     * to return a usable URI to the Excel file
     */
    private void importGroup() {
        startTime = System.currentTimeMillis();
        Intent intent = presenter.createImportIntent();
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 1);
    }

    /**
     * This function finishes the import process by
     * - Getting the name the user typed into the name box and saving it to the most recent
     * group to be added to the group list.
     * - Saving the group list
     * - Navigating to the list fragment
     * - Hiding the keyboard if it's been left open
     */
    private void finishImport(){
        if((System.currentTimeMillis() - startTime) > 3000) {
            EditText editText = getActivity().findViewById(R.id.editText);
            GroupManager.getGroups().get(GroupManager.getSize() - 1).addName(editText.getText().toString());
            GroupManager.saveGroups();
            NavHostFragment.findNavController(ImportFragment.this)
                    .navigate(R.id.action_import_to_list);
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
        else {
            Toast.makeText(getContext(), "Still importing. Please try again in 2 seconds", Toast.LENGTH_SHORT).show();
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
}
