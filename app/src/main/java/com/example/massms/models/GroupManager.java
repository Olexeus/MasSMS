package com.example.massms.models;

import android.content.Context;

import com.example.massms.main.List.ListContract;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class GroupManager{
    ListContract.View view;
    private static List<Group> groups = new ArrayList<Group>();
    private static Context context;
    private static DataManager dataManager;
    private static final Gson gson = new Gson();

    // Constructor takes in a view, along with any other dependencies
    public GroupManager(@org.jetbrains.annotations.NotNull Context context) {
        GroupManager.context = context;
        GroupManager.dataManager = new DataManager(GroupManager.context);
        if (groups.isEmpty()) {  // if groups are not defined retrieveGroups is called
            // it is not really singleton, but works only if defined in the main
            // and does not need to be redefined
            GroupManager.retrieveGroups();
        }
    }

    /*
     * Getters
     * */
    public static List<Group> getGroups() {
        return groups;
    }

    public static int getSize() { return groups.size(); }

    public static Group getGroup(String name) {
        int groupIndex = GroupManager.findGroup(name);
        if (groupIndex != -1) {
            return groups.get(groupIndex);
        }
        return null;
    }

    public static List<String> getGroupNames(){
        List<String> groupNames = new ArrayList<>();
        for(int i = 0; i < groups.size(); i++)
        {
            groupNames.add(groups.get(i).getGroupName());
        }
        return groupNames;
    }

    public static int findGroup(String name) {
        for (Group group : groups) {
            if (group.getFileName().equals(name) || group.getGroupName().equals(name)){
                return groups.indexOf(group);
            }
        }
        return -1;
    }

    /*
     * Setters
     * */
    public static void addGroup(Group newGroup) {
        groups.add(newGroup);
    }

    public static void addGroups(List<Group> newGroups) {
        groups.addAll(newGroups);
    }

    /*
     * Interact with memory
     * */
    public static void deleteGroup(String name) {
        // delete group even in local memory
        GroupManager.deleteGroup(GroupManager.findGroup(name));
    }

    public static void deleteGroup(int index) {
        // delete group even in local memory
        if (index < GroupManager.getSize() && index >= 0) {
            groups.remove(index);
        }
        GroupManager.saveGroups();
    }

    public static void saveGroups() {
        // Create JsonObject and append Groups.toJson. Then convert and save
        // do the same for retrieveGroups
        List<JsonObject> groupClasses = new ArrayList<JsonObject>();
        for (Group g: groups) {
            groupClasses.add(Group.toJson(g));
        }
        dataManager.writeToFile(gson.toJson(groupClasses));
    }

    private static void retrieveGroups() {
        GroupManager.groups = new ArrayList<Group>();
        if (GroupManager.context != null) {
            String fileData = dataManager.readFile();
            // data was saved as the JsonArray
            JsonArray groupClasses = gson.fromJson(fileData, JsonArray.class);
            // iterate through every JsonElement and convert it into Group object
            if(groupClasses != null) {
                for (JsonElement groupElement : groupClasses) {
                    Group newGroup = Group.fromJson(groupElement.getAsJsonObject());
                    // add group only if it is not empty
                    if (!newGroup.empty()) {
                        groups.add(newGroup);
                    }
                }
            }
        }
    }
}
