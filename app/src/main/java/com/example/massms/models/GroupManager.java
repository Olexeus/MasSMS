package com.example.massms.models;

import android.content.Context;

import com.example.massms.main.ListContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GroupManager{
    ListContract.View view;
    private static List<Group> groups = null;
    private static Context context;

    // Constructor takes in a view, along with any other dependencies
    public GroupManager() {
    }
    public GroupManager(Context context) {
        GroupManager.context = context;
        if (groups == null) {
            GroupManager.retrieveGroups();
            if (groups == null) {
                GroupManager.groups = new ArrayList<Group>();
            }
        }
        // this.groups = new ArrayList<Group>();
    }

    // getters
    public static List<Group> getGroups() {
        return groups;
    }

    public static Group getGroup(String name) {
        // TODO: needs testing
        int groupIndex = GroupManager.findGroup(name);
        /*if (groups.size() > groupIndex) {
            return groups.get(groupIndex);
        }*/
        if (groupIndex != -1) {
            return groups.get(groupIndex);
        }
        return null;
    }

    public static int findGroup(String name) {
        if (groups == null) {
            GroupManager.retrieveGroups();
        }
        for (Group group : groups) {
            if (group.getFileName().equals(name) || group.getGroupName().equals(name)){
                return groups.indexOf(group);
            }
        }
        return -1;
    }

    // setters
    public static void addGroup(Group newGroup) {
        if (groups == null) {
            GroupManager.retrieveGroups();
            if (groups == null) {
                GroupManager.groups = new ArrayList<Group>();
            }
        }
        GroupManager.groups = new ArrayList<Group>();
        groups.add(newGroup);
    }

    public static void addGroups(List<Group> newGroups) {
        if (groups == null) {
            GroupManager.retrieveGroups();
            if (groups == null) {
                GroupManager.groups = new ArrayList<Group>();
            }
        }
        groups.addAll(newGroups);
    }

    // interact with memory
    public void deleteGroup(String name) {
        // TODO: delete group even in local memory
        // TODO: needs testing
        groups.remove(GroupManager.getGroup(name));
    }

    public void saveGroups() {
        // TODO: Create JsonObject and append Groups.toJson. Then convert and save
        // do the same for retrieveGroups
        DataManager dataManager = new DataManager(GroupManager.context);
        Gson gson = new Gson();
        dataManager.writeToFile(gson.toJson(this));

        /*DataManager dataManager = new DataManager(GroupManager.context);
        groups = new ArrayList<Group>();
        List<JsonObject> groupClasses = new ArrayList<JsonObject>();
        Gson gson = new Gson();
        for (Group g: groups) {
            groupClasses.add(g.toJson());
        }
        dataManager.writeToFile(gson.toJson(groupClasses));*/
    }

    public static void retrieveGroups() {
        // this.groups = new ArrayList<Group>();
        if (GroupManager.context != null) {
            DataManager dataManager = new DataManager(GroupManager.context);
            String fileData = dataManager.readFile();
            Gson gson = new Gson();
            GroupManager groupManager = gson.fromJson(fileData, GroupManager.class);
            // this.groups = groupManager.getGroups();
        }
        /*if (GroupManager.context != null) {
            DataManager dataManager = new DataManager(GroupManager.context);
            String fileData = dataManager.readFile();
            Gson gson = new Gson();
            JsonObject groupClasses = gson.fromJson(fileData, JsonObject.class);

        }*/
    }
}
