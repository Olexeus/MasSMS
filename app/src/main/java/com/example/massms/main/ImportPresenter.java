package com.example.massms.main;

public class ImportPresenter implements ImportContract.Presenter {
    ImportContract.View view;

    // Constructor takes in a view, along with any other dependencies
    public ImportPresenter(ImportContract.View view) {
        this.view = view;
    }

    // Called by the view when it is created
    @Override
    public void onViewCreated() {

    }

    // Called by the view when it is created
    @Override
    public void onDestroy() {

    }
}
