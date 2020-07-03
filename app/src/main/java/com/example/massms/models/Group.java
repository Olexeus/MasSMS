package com.example.massms.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Group implements JsonDeserializer<Group> {
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

    public boolean empty() {
        return contacts.isEmpty();
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
    public Person getContact(Double phone) {
        for (Person contact : contacts) {
            if (contact.getPhone().equals(phone)){
                return contact;
            }
        }
        return null;
    }
    public Person getContact(int index) {
        return contacts.get(index);
    }
    public List<Person> getContacts() {
        return contacts;
    }
    public String getGroupName() { return groupName; }
    public String getFileName() { return fileName; }

    // conversion to JSON
    public static JsonObject toJson (Group group) {
        Gson gson = new Gson();
        JsonObject convertedGson = gson.fromJson(gson.toJson(group), JsonObject.class);
        return convertedGson;
    }

    // conversion from JSON
    public static Group fromJson (String convertJson) {
        // convertJson = "{\"Ward\":[{\"first\":\"Zach\",\"last\":\"Peterson\",\"phone\":9.132719504E9},{\"first\":\"Elder1\",\"last\":\"Smith\",\"phone\":5.551112222E9},{\"first\":\"Deacon1\",\"last\":\"Smith\",\"phone\":4.441113333E9}],\"groupName\":\"Smith\"}";
        Gson gson = new GsonBuilder().registerTypeAdapter(Group.class, new Group()).create();
        Group newGroup  = gson.fromJson(convertJson, Group.class);
        return newGroup;
    }

    public static Group fromJson (JsonObject convertJson) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Group.class, new Group()).create();
        return gson.fromJson(convertJson, Group.class);
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

    @Override
    public Group deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        // https://stackoverflow.com/questions/23472175/gson-deserializing-json-with-random-class-names
        final JsonObject convertedGson = json.getAsJsonObject();
        final Group newGroup = new Group();

        // Check for group name and delete it
        if (convertedGson.has("groupName")) {
            final String groupName = convertedGson.get("groupName").getAsString();
            convertedGson.remove("groupName");
            newGroup.addName(groupName);
        }
        if (convertedGson.has("fileName")) {
            // final String fileName = convertedGson.get("fileName").getAsString();
            convertedGson.remove("fileName");
        }

        // get the key with contacts
        Set<Map.Entry<String, JsonElement>> groupObjects =  convertedGson.entrySet();
        // get Persons as JsonArray
        JsonArray jsonContacts = convertedGson.get(groupObjects.iterator().next().getKey()).getAsJsonArray();

        // Convert jsonContacts into Person objects
        Gson gson = new GsonBuilder().registerTypeAdapter(Person.class, new Person()).create();
        List<Person> objectContacts = new ArrayList<>();
        for (JsonElement entry : jsonContacts) {
            JsonElement jsonElement  = entry;
            Person contact = gson.fromJson(jsonElement, Person.class);
            if (contact != null) {
                objectContacts.add(contact);
            }
        }

        // add converted data
        newGroup.addContacts(objectContacts);

        return newGroup;
    }

}
