package com.example.massms.main;

/**
 * Our MainPresenter will probably do very little (50-100 lines).
 * I think its still important to follow MVP though.
 * A bunch of small classes is generally better than 1 monolithic class
 */
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

    // Called by the view when it is destroyed
    @Override
    public void onDestroy() {

    }
}
