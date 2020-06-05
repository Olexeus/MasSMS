package com.example.massms;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupName;

    // TODO: handle random Sheet name
    @SerializedName(value="Ward", alternate={"Sheet 1", "Sheet 2"})
    private List<Person> contacts;

    Group() {
        groupName = null;
        contacts = new ArrayList<>();
    }

    // TODO: add people function for an array of people
    // TODO: get contact function

    @Override
    public String toString() {
        String groupString = "Group name: " + this.groupName;
        for (Person c : contacts) {
            groupString += c;
        }
        return groupString;
    }

}
