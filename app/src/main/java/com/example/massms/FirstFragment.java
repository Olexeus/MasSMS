package com.example.massms;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.File;
import java.util.Objects;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.import_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importGroup();
            }
        });
    }

    private void importGroup(){
        String[] mimeTypes =
                { "application/vnd.ms-excel",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 1);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1 && resultCode == -1) {

                //Zach, you can rename the file to whatever you want, but the file that the intent is
                //saving to is called xlsx and is created below. You can use this file as you see fit.
                //It's saved in MainActivity so you can access it anywhere
                //Probably create a new screen that you name the group in? Or if you think it'll be
                //better to name the group first, you can change the onClickListener above
                //to launch a new activity and from there call importGroup and move this method onActivityResult
                //to the new activity too. IDK why this is all in fragments, Oleksii started it this way
                //I guess change it if it's hard to work with? The advantage of fragments is we can have
                //the same MainActivity but show different things depending on if the user has imported
                //Their first group or not

                MainActivity.xlsx = new File(Objects.requireNonNull(Objects.requireNonNull(data.getData()).getPath()));
                Toast.makeText(getActivity(), data.getData().toString(), Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);

            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
