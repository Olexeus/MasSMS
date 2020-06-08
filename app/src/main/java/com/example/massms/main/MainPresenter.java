package com.example.massms.main;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;

    // Constructor takes in a view, along with any other dependencies
    public MainPresenter(MainContract.View view) {
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
