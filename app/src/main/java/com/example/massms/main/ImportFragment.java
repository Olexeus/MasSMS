package com.example.massms.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.massms.R;

public class ImportFragment extends Fragment implements ImportContract.View {
    private ImportContract.Presenter presenter;
    private Button importButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // I'm not sure whether these 2 methods would go in here or onViewCreated since I still don't fully understand fragments.
        setPresenter(new ImportPresenter(this));
        presenter.onViewCreated();

        // Still not sure whether this kind of stuff belongs here or the next method.
        importButton = container.findViewById(R.id.import_btn);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importGroup();
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1 && resultCode == -1) {
                // Not sure if this should be where this is called
                presenter.convertExcelToJson(data, getContext());

                NavHostFragment.findNavController(ImportFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
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
        Intent intent = createImportIntent();
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 1);
    }

    // this intent stuff probably stays in the view
    private Intent createImportIntent() {
        Intent intent;
        intent = new Intent(Intent.ACTION_GET_CONTENT);

        String[] mimeTypes = { "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }
}
