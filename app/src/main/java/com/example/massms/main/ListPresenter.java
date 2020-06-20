package com.example.massms.main;

import com.example.massms.models.Group;

import java.util.ArrayList;
import java.util.List;

public class ListPresenter implements ListContract.Presenter {
    ListContract.View view;
    private List<Group> groups;

    // Constructor takes in a view, along with any other dependencies
    public ListPresenter(ListContract.View view) {
        this.view = view;
        this.groups = new ArrayList<Group>();
    }

    // Called by the view when it is created
    @Override
    public void onViewCreated() {
    }

    // Called by the view when it is destroyed
    @Override
    public void onDestroy() {
    }

    // getters
    public List<Group> getGroups() {
        return groups;
    }

    public Group getGroup(String name) {
        // TODO: needs testing
        int groupIndex = this.findGroup(name);
        /*if (groups.size() > groupIndex) {
            return groups.get(groupIndex);
        }*/
        if (groupIndex != -1) {
            return groups.get(groupIndex);
        }
        return null;
    }

    public int findGroup(String name) {
        for (Group group : groups) {
            if (group.getFileName().equals(name) || group.getGroupName().equals(name)){
                return groups.indexOf(group);
            }
        }
        return -1;
    }

    // setters
    public void addGroup(Group newGroup) {
        groups.add(newGroup);
    }

    public void addGroups(List<Group> newGroups) {
        groups.addAll(newGroups);
    }

    // interact with memory
    public void deleteGroup(String name) {
        // TODO: delete group even in local memory
        // TODO: needs testing
        groups.remove(this.getGroup(name));
    }

    public void saveGroups() {
        // TODO: finish saving into memory when DataManager is done
        // TODO: pass class by reference for higher level of abstraction
    }

    public void retrieveGroups() {

    }
}
