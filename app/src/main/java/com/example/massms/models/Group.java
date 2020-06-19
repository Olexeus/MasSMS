package com.example.massms.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Group {
    @SerializedName("groupName")
    private String groupName;
    @SerializedName("fileName")
    private String fileName;
    @SerializedName("contacts")
    private List<Person> contacts;

    public Group() {
        groupName = null;
        contacts = new ArrayList<>();
    }

    public void addName(String newGroupName) {
        this.groupName = newGroupName;
        this.fileName = this.groupName.replaceAll(" ", "_").toLowerCase();
    }

    // add contact function for a single contact
    public void addContact(Person newContact) {
        contacts.add(newContact);
    }

    // add contacts function for an array of contacts
    public void addContacts(List<Person> newContacts) {
        contacts.addAll(newContacts);
    }

    // getContact and getContacts methods
    public Person getContact(int index) {
        return contacts.get(index);
    }
    public List<Person> getContacts() {
        return contacts;
    }

    // conversion to JSON
    public JsonObject toJson () {
        Gson gson = new Gson();
        JsonObject convertedGson = new Gson().fromJson(gson.toJson(this), JsonObject.class);
        return convertedGson;
    }

    public static Group fromJson (String convertJson) {
        convertJson = "{\"Ward\":[{\"first\":\"Zach\",\"last\":\"Peterson\",\"phone\":9.132719504E9},{\"first\":\"Elder1\",\"last\":\"Smith\",\"phone\":5.551112222E9},{\"first\":\"Deacon1\",\"last\":\"Smith\",\"phone\":4.441113333E9}],\"groupName\":\"Smith\"}";
        String JsonString = convertJson;
        Gson gson = new GsonBuilder().registerTypeAdapter(Group.class, new CustomDeserializer()).create();
        Group newGroup = null;

        // 1: First Sloppy change of name
        /*// Changes the first name in the String to the list of contacts
        boolean nameChanged = false;
        char current, previous;

        // Create an array from string and loop through it
        char[] JsonArray = JsonString.toCharArray();
        JsonString = new String();
        for (int i = 0; i < JsonArray.length; i++) {
            current = JsonArray[i];
            if (i >= 1 && i < 100) {
                previous = JsonArray[i - 1];
                if (previous == '{' && current == '\"' && nameChanged == false) {
                    nameChanged = true;
                    JsonString += (current + "contacts");
                    do {
                        i++;
                    } while (JsonArray[i] != '\"');
                }
            }
            JsonString += current;
        }*/

        // 2: Second Sloppy change of name
        /*// convert string to JSONObject
        JSONObject stringToJson = null;
        try {
            stringToJson = new JSONObject(convertJson);
        } catch (JSONException e) {
            Log.d("String to JSON", e.getMessage());
        }
        // get the key of contacts
        Iterator<String> keys = stringToJson.keys();
        String contactsKey = keys.next();
        // get the value of contacts
        JSONArray contacts = null;
        try {
            contacts = stringToJson.getJSONArray(contactsKey);
        } catch (JSONException e) {
            Log.d("Insert contacts", e.getMessage());
        }
        // remove contacts with random key
        stringToJson.remove(contactsKey);
        // insert contacts with needed key
        try {
            stringToJson.put("contacts", contacts);
        } catch (JSONException e) {
            Log.d("Insert contacts", e.getMessage());
        }
        JsonString = stringToJson.toString();
*/

        // 3: Thirst Sloppy solution
        /*
        JsonObject convertedGson = new Gson().fromJson(convertJson, JsonObject.class);
        Set<Map.Entry<String, JsonElement>> groupObjects =  convertedGson.entrySet();
        JsonArray jsonContacts = convertedGson.get(groupObjects.iterator().next().getKey()).getAsJsonArray();
        // Set<Map.Entry<String, JsonElement>> listContacts = jsonContacts.entrySet();
        List<Person> objectContacts = new ArrayList<>();
        for (JsonElement entry : jsonContacts) {
            JsonElement jsonElement  = entry;
            Person contact = gson.fromJson(jsonElement, Person.class);
            objectContacts.add(contact);
        }
        newGroup = new Group();
        newGroup.addContacts(objectContacts);
        newGroup.addName(convertedGson.remove("groupName").toString());*/



        // Creates a new Group class
        newGroup = gson.fromJson(JsonString, Group.class);
        return newGroup;
    }

    public static Group fromJson (JsonObject convertJson) {
        String JsonString = convertJson.toString();
        return Group.fromJson(JsonString);
    }

    // TODO: comparator

    @Override
    public String toString() {
        String groupString = String.format("N%n%nGroup name: %s"
                , this.groupName);
        for (Person c : contacts) {
            groupString += c;
        }
        return groupString;
    }

}
