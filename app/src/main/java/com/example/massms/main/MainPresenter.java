package com.example.massms.main;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;

    // Constructor takes in a view, along with any other dependencies
    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    // Sets properties necessary to import an Excel file
    private void setPoiProperties() {
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
    }

    // Called by the view when it is created
    @Override
    public void onViewCreated() {
        setPoiProperties();
    }

    // Called by the view when it is destroyed
    @Override
    public void onDestroy() {

    }
}
