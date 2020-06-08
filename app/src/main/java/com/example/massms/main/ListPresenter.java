package com.example.massms.main;

public class ListPresenter implements ListContract.Presenter {
    ListContract.View view;

    // Constructor takes in a view, along with any other dependencies
    public ListPresenter(ListContract.View view) {
        this.view = view;
    }

    // Called by the view when it is created
    @Override
    public void onViewCreated() {
        // TODO
    }

    // Called by the view when it is destroyed
    @Override
    public void onDestroy() {
        // TODO
    }
}
